require "mobiletest/version"
Dir[File.dirname(__FILE__) + '/mobiletest/**/*.rb'].each do |file|
  require_relative file
end

module Mobiletest
  # Your code goes here...
end
