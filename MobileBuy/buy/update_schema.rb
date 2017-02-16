#!/usr/bin/env ruby

require 'graphql_java_gen'
require 'graphql_schema'
require 'json'
require 'optparse'
require 'net/http'
require 'fileutils'

domain = "app.shopify.com"
schema_filename = nil
target_filename = nil
ROOT_DIR = File.expand_path("../", __FILE__)
CACHED_SCHEMA_FILENAME = "#{ROOT_DIR}/build/intermediates/graphql/graphql_schema.json"

OptionParser.new do |opts|
  opts.on("-fFILENAME", "--file=FILENAME", "Get schema from file") do |filename|
    schema_filename = filename
    domain = nil
  end
  opts.on("-c", "--cached-schema", "Used cached schema from last run") do |filename|
    schema_filename = CACHED_SCHEMA_FILENAME
    domain = nil
  end
  opts.on("-tFILENAME", "--target=FILENAME", "Target file name") do |filename|
    target_filename = filename
    #domain = nil
  end
end.parse!

dirname = File.dirname(CACHED_SCHEMA_FILENAME)
unless File.directory?(dirname)
  FileUtils.mkdir_p(dirname)
end

if domain
  schema_filename = CACHED_SCHEMA_FILENAME
  body = Net::HTTP.get(URI("https://#{domain}/services/ping/storefront_graphql_schema"))
  File.write(schema_filename, JSON.pretty_generate(JSON.parse(body)))
end

schema = GraphQLSchema.new(JSON.parse(File.read(schema_filename)))

GraphQLJavaGen.new(schema,
  package_name: "com.shopify.buy3",
  nest_under: 'APISchema',
  custom_scalars: [
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
).save(target_filename)
