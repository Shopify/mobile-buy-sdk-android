#!/usr/bin/env ruby

require 'ADB'

class AndroidEmulator
  include ADB
  attr_accessor :config

  def initialize(config={})
    @config = config
    set_default_config
  end

  def exists?(name)
    emulator_check = shell("get-state")
    emulator_check != "device"
  end

  def set_default_config
    @config[:name]            ||= "screenshot"
    @config[:android_version] ||= "android-21"
    @config[:abi]             ||= "default/x86"
    @config[:sd_card_size]    ||= "512M"
    @config[:emulator_port]   ||= 5560
    @config[:avd_path]        ||= File.expand_path("~/.android/avd/screenshot.avd")
    @config[:sd_path]         ||= "#{@config[:avd_path]}/screenshot-sdcard.img"
    @config[:android_home]    ||= ENV["ANDROID_HOME"]
    puts "Android Home is at #{@config[:android_home]}"
  end

  def create(should_force=false)
    create_emulator(should_force)
    create_sd_card
  end

  def create_emulator(force=false)
    destroy! if force

    unless Dir.exist?(@config[:avd_path])
      Process.fork do
        puts "Creating Android Virtual Device (AVD) for screenshot testing"
        emulator_args = [
          '#{@config[:android_home]}/tools/android',
          'create',
          'avd',
          '-n', @config[:name],
          '-t', "\"#{@config[:android_version]}\"",
          '--abi', @config[:abi]
        ]
        system "echo 'n' | #{emulator_args.join(" ")}"
      end
      Process.wait
      open("#{@config[:avd_path]}/config.ini", 'a') { |f| f.puts "hw.keyboard=yes" }
    else
      puts "Emulator image already exists"
    end
  end

  def create_sd_card
    unless !File.exist?(@config[:sd_path])
      puts "Creating sdcard"
      Process.fork do
        puts "Spawning child procress to start emulator"
        exec "#{@config[:android_home]}/tools/mksdcard -l sdcard #{@config[:sd_card_size]} \"#{@config[:sd_path]}\""
      end
      Process.wait
    else
      puts "SD Card already exists"
    end
  end 

  def destroy!(name=@config[:name])
    system "android delete avd -n #{name}"
    FileUtils.rm_rf(@config[:avd_path])
  end

  def destroy_sd_card!
    FileUtils.rm_rf(@config[:sd_path])
  end

  def setup
    puts "--- Set up emulator"
    puts "Unlock the emulator" and shell("input keyevent 82")
    puts "Don't show passwords" and shell("settings put system show_password 0")
  end

  def start
    emulator_args = ['#{@config[:android_home]}/tools/emulator', 
                     '-avd', @config[:name],
                     '-sdcard', @config[:sd_path],
                     '-port', @config[:emulator_port].to_s,                    
                     '-no-audio', '-gpu on']
    
    Process.fork do
      puts "Spawning child procress to start emulator"
      puts emulator_args.join(" ")
      exec emulator_args.join(" ")
    end

    puts "Waiting for emulator to start (max 360s)"
    wait_for_device({serial: devices[0]}, 360)
    setup
    disable_animations
    Process.wait
  end

  def disable_animations
    puts "Disable emulator animations"
    begin
      apk = File.expand_path("../AnimationDisabler.apk", __FILE__)
      puts "Installing from #{apk}"
      install(apk)
    rescue ADB::ADBError => err
      if err.message.include?("INSTALL_FAILED_ALREADY_EXISTS")
        puts "AnimationDisabler.apk already installed"
      else
        puts err
      end
    end
    shell("pm grant com.shopify.AnimationDisabler android.permission.SET_ANIMATION_SCALE")
    shell("am start -n com.shopify.AnimationDisabler/com.shopify.AnimationDisabler.DisableAnimationActivity")
  end
end
