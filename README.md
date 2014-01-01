Small utility applying LZMA compression to a given swf file. Accepts both uncompressed and zlib compressed input swf.

Useful when impossible to use ASC 2.0 or Flash Professional (that is when you are stuck with 1.0 version of compiler).

Usage: java -jar zlib2lzma -i file:///C:/inputFile.swf -o file:///C:/outputFile.swf

It's pretty much a manual Java port of a utility described here: http://blog.kaourantin.net/?p=124#respond. In some situations, like a platform-agnostic build process it's useful to stick to java. Code is meant to be self-explenatory.

Sourcode has 2 dependencies:
- commons-cli 1.2 maintained by apache
- lzma-sdk 9.2 maintained by 7zip

Both of them are included in AIR SDK (currently part of the Adobe Gaming SDK 1.3). Attached in libs for conveniance.

If you need a flash version of this utility I highly recommend to go to http://blog.kaourantin.net/?p=124#respond, grab the C code and transcompile it using Flascc/Alchemy2/Crossbridge which is a LLVM-based cross-compiler available here https://github.com/adobe-flash/crossbridge.
