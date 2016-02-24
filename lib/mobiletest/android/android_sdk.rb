#!/usr/bin/env ruby

class AndroidSDK

  def initialize(config={})
    @config = config
    @config[:sdk_packages] ||= ["android-21", "sys-img-x86-android-21"]
  end

  def update!
    puts "Updating SDK"
    @config[:sdk_packages].each do |package|
      puts "Updating #{package}"
      system "echo y | android --silent update sdk --no-ui --all --filter \"#{package}\" &>/dev/null"
    end
  end

end
