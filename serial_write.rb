#!/usr/bin/env ruby

require 'serialport'

device = "/dev/ttyS2"

hex_msg = nil

ARGV.each do |a|
  if a.start_with? "d="
    device = a[2..-1]
  elsif a.match(/^(0x)?[0-9a-fA-F]+$/)
    hex_msg = a.gsub(/^0x/,'')
  end
end

if hex_msg.nil?
  puts "Usage: [d=/dev/ttyXX] 0x0123456789ABCDEF"
else
  sp = SerialPort.new(device, 9600, 8, 1, SerialPort::NONE)
  
  trap "SIGINT" do
    puts "Closing port"
	sp.close if !sp.nil?
    exit 130
  end
  
  hex_msg.scan(/../).map(&:hex)
  [hex_msg].pack('H*').unpack('C*')
  [hex_msg].pack('H*').bytes.to_a.each do |b|
    sp.putc(b)
	puts "wrote: 0x%x (%d %bb)" % [b, b, b]
  end
  
  sleep(1)
  
  puts "\n"
  while sp.any?
    puts sp.readline
  end
  sp.close
end

# loop do
  # sp.putc(i)
  # puts 'Wrote: %d = %bb' % [ i, i ]
  # i = (i == 15) ? 0 : (i + 1)
  # sleep(1)
# end