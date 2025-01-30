# frozen_string_literal: true

class GraphQLJavaGen
  class Annotation
    attr_reader :name, :imports

    def initialize(name, imports: [], &block)
      @name = name
      @imports = imports
      @condition = block
    end

    def annotate?(field)
      @condition.call(field)
    end
  end
end
