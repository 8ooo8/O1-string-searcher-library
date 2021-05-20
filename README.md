# O(1) String Searcher Library

O(1) String Searcher provides a data structure which grants an **O(1)-time-complexity string search** but with an **O(n^2)-time-complexity string insertion**. One of its good fit scenarios is the chat history search function.

## Table of content

1. [Build library](#build-library)
1. [Library usage](#library-usage)
1. [Algorithm](#algorithm)
1. [The O(1)-time-complexity variation](#the-o1-time-complexity-variation)
1. [Class diagram](#class-diagram)
1. [Extensibility](#extensibility)
1. [Reminder](#reminder)
1. [License](#license)

## Build library

1. Check Java version.
    ```bash
    java -version
    ```
1. Update Java if version below 11.
1. Build the library with javac.
    ```bash cd <O(1)-string-searcher-directory>
    javac --module-path junittest/dependencies/junit-4.10.jar \
        --add-modules junit \
        --patch-module junit=junit-4.10.jar \
        --module-source-path . $(find instantstringsearcher -name "*.java")  $(find junittest -name "*.java") \
        -d build
    ```

## Library usage

1. Import the library
    ```java
    import github.eightoooeight.instantstringsearcher.*;
    ```
1. Create an instance of InstantStringSearcher
    ```java
    IInstantStringSearcher instantStringSearcher = InstantStringSearcher.getInstance();
    ```
1. Insert strings
    ```java
    instantStringSearcher.insertString(toInsert);
    ```
1. Search strings
    ```java
    <List>String searchResult = instantStringSearcher.searchtString(strToSearch);
    ```

## Algorithm

![Algorithm figure][algo-figure]

Consider that this string searcher is particularly useful only when the string amount is too massive, this library is designed to:

1. save the trie into the persistent storage, and
1. to be able to load just the node that is responsible to the string search

so that the RAM/swapping will not be overwhelmed and a quick node loading can be guaranteed.

## The O(1)-time-complexity variation

There are various possilbe implementations of the above [algorithm](#algorithm) and in this library, one of the O(1)-variations is implemented.

The O(1)-variation 1:

1. O(1) function to locate the node file that is responsible to the string search.
    1. Store the node with keychain, node-keychain, into the file, \<Trie-file-directory-path\>/\<node-keychain\>.txt.
    1. Get the node filepath by concatenating the Trie-file-directory-path with /node-keychain.txt, which is in O(1) time complexity.
1. Store the strings instead of the string IDs into the node file so that the search result can obtained by simply loading the file. This gives an absolute O(1) time complexity but with a higher space complexity and various limitations.

**The O(1)-variation 2 (the variation implemented)**:

1. O(1) function to locate the node file that is responsible to the string search.
    1. Store the node with keychain, node-keychain, into the file, \<Trie-file-directory-path\>/\<node-keychain\>.txt.
    1. Get the node filepath by concatenating the Trie-file-directory-path with /node-keychain.txt, which is in O(1) time complexity.
1. Unlike variation 1, the string IDs are stored into the node files and hence, the search result cannot be obtained by simply loading the node file.
1. A file, rawData.txt, is used to store the inserted strings. Every time a string is inserted, the string is appended to this file.
1. Using the string IDs to retrieve strings from rawData.txt.
1. The string IDs are in the form of \<start-position\>-\<end-position\>, where the start-position refers to the starting position of its associated string in rawData.txt, and similar for the end-position.
1. Using random access to read string from the start-position to the end-position in rawData.txt to retrieve the string, which gives an "O(1)" time complexity (not absolutely O(1) as it varies with persistent storage fragmentation).
1. O(1) function to locate the node file + O(1) function to retrieve the strings using the string IDs, i.e. an overall O(1) time complexity is achieved.

**The limitations of the O(1)-variation 2**:

1. In this library, the node file names are the node keychains. Consequently, it will not be able to handle the strings with characters invalid to be a part of the filenames.
1. In this library, the node file names are the node keychains. Consequently, it will not be able to handle lengthy strings due to the filepath and filename length limitation from the filesystems, e.g. maximum 255 filename length on EXT4 on Linux.

**The possible solutions to the limitations**:

1. Not directly using the keychain to be the filename of the node files to resolve the limitation (1).
1. Add a dictionary layer between the nodes and the node files to resolve both limitation (1) & (2).

## Class diagram

![Class diagram][class-diagram]

## Extensibility

1. This library adopts SOLID principles and it is suggested to extend its feature(s) in an open-closed manner, e.g. to resolve the limitation (1) mentioned in [The O(1)-time-complexity variation](#the-o1-time-complexity-variation), create a new class implementing ITrieNodeAndFilepathMapper to remap the node and the node files.
1. To facilitate the development, a simple JUnit test module is provided. Add your own test cases into the module and run below commands to test the library.
    1. Build the library with javac.
        ```bash cd <O(1)-string-searcher-directory>
        javac --module-path junittest/dependencies/junit-4.10.jar \
            --add-modules junit \
            --patch-module junit=junit-4.10.jar \
            --module-source-path . $(find instantstringsearcher -name "*.java")  $(find junittest -name "*.java") \
            -d build
        ```
    1. Run the Junit to test the library.
        ```bash
        java --module-path build:junittest/dependencies -m junittest/github.eightoooeight.instantstringsearcher.junittest.TestRunner
        ```

## Reminder

Please note that this library is case-sensitive and does not remove the duplicate search result, e.g. it returns two "eye" upon a search of "e". If case-insensitiveness or distinct search result is expected, a post processing of the search result is needed.

## License

[MIT][MIT-license]

[MIT-license]: LICENSE
[class-diagram]: docs/class-diagram.png
[algo-figure]: docs/algo.png
