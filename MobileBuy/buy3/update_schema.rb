#!/usr/bin/env ruby

require 'bundler/setup'
require 'graphql_java_gen'
require 'graphql_schema'
require 'json'
require 'optparse'
require 'net/http'
require 'fileutils'

target_filename = '../buy3/src/main/java/com/shopify/buy3/Storefront.java'
OptionParser.new do |opts|
  opts.on("-tFILENAME", "--target=FILENAME", "Target file name") do |filename|
    target_filename = filename
  end
end.parse!

shared_storefront_api_key = "4a6c829ec3cb12ef9004bf8ed27adf12"
storefront_api_version = ARGV[0]

abort("Error: API Version not specified") if storefront_api_version.nil? or storefront_api_version.empty?

uri = URI("https://app.shopify.com/services/graphql/introspection/storefront?api_client_api_key=#{shared_storefront_api_key}&api_version=#{storefront_api_version}")

response = Net::HTTP.get_response(uri)
abort("Error fetching details for the api version #{storefront_api_version}") unless response.kind_of? Net::HTTPSuccess

schema = GraphQLSchema.new(JSON.parse(response.body))
custom_scalars = [
  GraphQLJavaGen::Scalar.new(
    type_name: 'Money',
    java_type: 'BigDecimal',
    deserialize_expr: ->(expr) { "new BigDecimal(jsonAsString(#{expr}, key))" },
    imports: ['java.math.BigDecimal'],
  ),
  GraphQLJavaGen::Scalar.new(
    type_name: 'DateTime',
    java_type: 'DateTime',
    deserialize_expr: ->(expr) { "Utils.parseDateTime(jsonAsString(#{expr}, key))" },
    imports: ['org.joda.time.DateTime'],
  )
]

GraphQLJavaGen.new(
  schema,
  package_name: "com.shopify.buy3",
  nest_under: 'Storefront',
  custom_scalars: custom_scalars,
  include_deprecated: true,
  version: storefront_api_version
).save(target_filename)
