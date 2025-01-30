# frozen_string_literal: true

class GraphQLJavaGen
  class Scalar
    attr_reader :type_name, :imports

    WRAPPER_OBJECT = {
      "int" => "Integer",
      "long" => "Long",
      "double" => "Double",
      "boolean" => "Boolean",
    }
    WRAPPER_OBJECT.default_proc = ->(_, key) { key }
    private_constant :WRAPPER_OBJECT

    def initialize(type_name:, java_type:, deserialize_expr:, imports: [])
      @type_name = type_name
      @java_type = java_type
      @deserialize_expr = deserialize_expr
      @imports = imports
    end

    def nullable_type
      WRAPPER_OBJECT[@java_type]
    end

    def non_nullable_type
      @java_type
    end

    def deserialize(expr)
      @deserialize_expr.call(expr)
    end
  end
end
