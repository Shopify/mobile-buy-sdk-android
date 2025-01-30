# frozen_string_literal: true

class GraphQLJavaGen
  # Reformat code that uses curly brace blocks
  class Reformatter
    INDENT_START_CHARS = ["{", "("]
    INDENT_END_CHARS = ["}", ")"]

    def initialize(indent: "\t")
      @indent = indent
    end

    def reformat(code)
      output = String.new
      indent_level = 0
      squeeze_newlines = true

      code.lines.each do |line|
        stripped_line = line.strip

        if INDENT_END_CHARS.include?(stripped_line[0])
          indent_level -= 1
          # no blank lines immediately preceding end of block
          output.rstrip!
          output << "\n"
        end

        if stripped_line.empty?
          output << "\n" unless squeeze_newlines
          squeeze_newlines = true
        else
          output << @indent * indent_level << line.lstrip
          squeeze_newlines = false
        end

        next unless INDENT_START_CHARS.include?(stripped_line[-1])

        indent_level += 1
        # no blank lines following start of block
        squeeze_newlines = true
      end
      output
    end
  end
end
