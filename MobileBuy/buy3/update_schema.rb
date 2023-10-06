#!/usr/bin/env ruby

require 'bundler/setup'
require 'graphql_java_gen'
require 'graphql_schema'
require 'json'
require 'optparse'
require 'faraday'
require 'fileutils'

target_filename = '../buy3/src/main/java/com/shopify/buy3/Storefront.java'
OptionParser.new do |opts|
  opts.on("-tFILENAME", "--target=FILENAME", "Target file name") do |filename|
    target_filename = filename
  end
end.parse!

storefront_domain = "graphql.myshopify.com"
storefront_access_token = "3a109d5ebb8117f935adc1df233f3dfc"
storefront_api_version = ARGV[0]

introspection_query = File.read(File.join(__dir__, 'introspection.graphql'))

conn = Faraday.new(
  url: "https://#{storefront_domain}/api/#{storefront_api_version}/",
  headers: {
    'Accept' => 'application/json',
    'Content-Type' => 'application/graphql',
    'X-Shopify-Storefront-Access-Token' => storefront_access_token
  }
)

res = conn.post('graphql') do |req|
  req.body = introspection_query
end

abort "failed to introspect schema" unless res.success?

schema = GraphQLSchema.new(JSON.parse(res.body))

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
