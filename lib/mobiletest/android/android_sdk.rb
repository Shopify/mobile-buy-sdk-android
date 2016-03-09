#!/usr/bin/env ruby
# encoding: utf-8

class AndroidSDK
  attr_accessor :packages

  def install(*packages)
    puts "Updating SDK"
    packages.each do |package|
      unless installed?(package)
        puts "Updating #{package}"
        system "echo y | android --silent update sdk --no-ui --all --filter \"#{package}\" &>/dev/null"
      else
        puts "#{package} already installed"
      end
    end
  end

  def installed?(package)
    package_info = packages.detect { |p| p[:name] == package }
    raise "Package '#{package}' doesn't exist" unless package_info
    
    package_path = package_info[:install_path] ? package_info[:install_path] : package_info[:type]
    path_to_check = File.join(ENV['ANDROID_HOME'], package_path, package_info[:name])
    puts path_to_check
    Dir.exist?(path_to_check)
  end

  def packages
    return @packages if @packages

    packages, stderr_str, status = Open3.capture3("android list sdk --no-ui --all -e")

    packages.gsub!(/(.|\n)*Packages available for installation or update: \d+/,'')
    packages = packages.split(/-{2,}/)
    packages = packages.map { |t| t.split("\n").reject(&:empty?).map(&:strip) }

    @packages = packages.collect do |t|
      next if t.empty?

      match = /id: (\d+) or \"(.*)"/.match(t.first)
      type = t[1].split(": ").last
      desc = t[2].split(": ").last
      install_path = nil

      for i in 2..t.length
        line = t[i]
        if line
          desc << " #{line}"
          install_path = line.split(": ").last if line.include?("Install path")
        end
      end

      { id: match[1], name: match[2], type: type_map(type), desc: desc, install_path: install_path }
    end.reject(&:nil?)
  end

  def type_map(type)
    { 'Addon' => 'add-ons',
      'SystemImage' => 'system-images',
      'Sample' => 'samples',
      'Platform' => 'platforms',
      'BuildTool' => 'build-tools',
      'Doc' => 'docs',
      'Tool' => 'tools',
      'PlatformTool' => 'platform-tools',
      'Source' => 'sources',
      'Extra' => 'extras' }[type]
  end
end
