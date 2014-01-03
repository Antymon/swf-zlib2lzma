Small utility applying LZMA compression to a given swf file.

Useful when impossible to use ASC 2.0 or Flash Professional (both of which give you an option for lzma compression).

Usage: java -jar zlib2lzma -i C:/inputFile.swf -o C:/outputFile.swf -v

-o path Mandatory output swf file. Can be the same as input. Output swf is lzma compressed.
-i path Mandatory input swf file. Can be uncompressed or zlib compressed
-v 	Flag stands for verbose and is optional. In verbose mode compression progression is reported.

Utility presented is pretty much a manual Java port of a utility described here: http://blog.kaourantin.net/?p=124#respond. In some situations, like a platform-agnostic build process it's useful to stick to java. Code is meant to be self-explenatory.

Sourcode has 2 dependencies:
- commons-cli 1.2 maintained by apache
- lzma-sdk 9.2 maintained by 7zip

Both of them are included in AIR SDK (currently part of the Adobe Gaming SDK 1.3). Attached in libs for conveniance.

If you need a flash version of this utility you can either use LZMA compression on ByteArray introduced in Flash Player 11.3/AIR 3.3 or if you prefer harder/more customizable way you can go to http://blog.kaourantin.net/?p=124#respond, grab the C code and transcompile it using Flascc/Alchemy2/Crossbridge which is a LLVM-based cross-compiler available here https://github.com/adobe-flash/crossbridge.
