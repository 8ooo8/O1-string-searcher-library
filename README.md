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
        --module-source-path . $(find instantstringsearcher -name "*.java") \
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

1. The flow of the string search
    1. [locate the node file] -> [load the file]
1. [locate the node file]
    1. The filepaths of the node files have to be in form of:  \<trie-file-directory-path\>/\<node-keychain\>.txt.
    1. Locate the node file, i.e. get the filepath, by concatenating the trie-file-directory-path with /node-keychain.txt, which is in O(1) time complexity.
1. [load the file]
    1. In the node files, store the strings instead of the string IDs so that the search result can be immediately obtained by loading the node file.

This variation grants an absolute O(1) time complexity but with a higher space complexity and various limitations.

**The O(1)-variation 2 (the variation implemented)**:

1. The flow of the string search
    1. [locate the node file] -> [load the file] -> [retrieve the strings with the string IDs]
1. [locate the node file]
    1. The filepaths of the node files have to be in form of:  \<trie-file-directory-path\>/\<node-keychain\>.txt.
    1. Locate the node file, i.e. get the filepath, by concatenating the trie-file-directory-path with /node-keychain.txt, which is in O(1) time complexity.
1. [load the file]
    1. In the node files, store the strings IDs.
1. [retrieve the strings with the string IDs]
    1. A file, rawData.txt, is used to store the inserted strings. Every time a string is inserted, the string is appended to this file.
    1. Using the string IDs from the node files to retrieve strings from rawData.txt.
    1. The string IDs are in form of \<string-start-position\>-\<string-end-position\>, where the string-start-position refers to the starting position of its associated string in rawData.txt, and similar for the string-end-position.
    1. Using random access to read rawData.txt starting from the string-start-position to the string-end-position to retrieve the string, which gives an "O(1)" time complexity (not absolutely O(1) as the reading speed depends on the persistent storage fragmentation).

**The limitations of the O(1)-variation 2**:

1. The node filenames are the node keychains. Consequently, it will not be able to handle the strings with characters invalid to be a part of the filenames.
1. The node filenames are the node keychains. Consequently, it will not be able to handle lengthy strings due to the filepath and filename length limitation from the filesystems, e.g. maximum 255 filename length on EXT4 on Linux.

**The possible solutions to the limitations**:

1. Not directly using the keychains to be the node filenames to resolve the limitation (1).
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
