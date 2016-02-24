#!/usr/bin/env ruby

class AndroidSDK

  def initialize(config={})
    @config = config
    @config[:android_versions] ||= ["android-21", "sys-img-x86-android-21"]
  end

  def update!
    puts "Updating SDK"
    @config[:android_versions].each do |version|
      puts "Updating #{version}"
      system "echo y | android --silent update sdk --no-ui --all --filter \"#{version}\" &>/dev/null"
    end
  end

end
