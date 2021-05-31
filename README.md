# O(1) String Searcher Library

O(1) String Searcher provides a data structure which grants an **O(1)-time-complexity string search** but with an **O(n^2)-time-complexity string insertion**. One of its good fit scenarios is the chat history search function.

## Table of content

1. [Build library](#build-library)
1. [Library usage](#library-usage)
1. [Algorithm](#algorithm)
1. [Class diagram](#class-diagram)
1. [The O(1)-time-complexity variation](#the-o1-time-complexity-variation)
1. [The improved synchronization variation](#the-improved-synchronization-variation)
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
1. Create an instance of [IInstantStringSearcher][i-instant-string-searcher-java]
    ```java
    IInstantStringSearcher instantStringSearcher = O1StringSearcher.getInstance();
    // or IInstantStringSearcher instantStringSearcher = ImprovedSyncStringSearcher.getInstance();
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

## Class diagram

![Class diagram][class-diagram]

## The O(1)-time-complexity variation

There are various possilbe implementations of the above [algorithm](#algorithm) and in this library, one of the O(1)-variations is implemented.

The O(1)-variation 1:

1. The flow of the string search:
    1. [locate the node file] -> [load the node file]
    1. [locate the node file]
        1. The filepaths of the node files have to be in form of:  \<trie-file-directory-path\>/\<node-keychain\>.txt.
        1. Locate the node file, i.e. get the filepath, by concatenating the trie-file-directory-path with /node-keychain.txt, which is in O(1) time complexity.
    1. [load the node file]
        1. In the node files, store the strings instead of the string IDs so that the search result can be immediately obtained by loading the node file.

This variation grants an absolute O(1) time complexity but with a higher space complexity and various limitations.

**The O(1)-variation 2 (the variation implemented)**:

1. The flow of the string search:
    1. [locate the node file] -> [load the node file] -> [retrieve the strings using the string IDs]
    1. [locate the node file]
        1. The filepaths of the node files have to be in form of:  \<trie-file-directory-path\>/\<node-keychain\>.txt.
        1. Locate the node file, i.e. get the filepath, by concatenating the trie-file-directory-path with /node-keychain.txt, which is in O(1) time complexity.
    1. [load the node file]
        1. In the node files, store the strings IDs.
    1. [retrieve the strings using the string IDs]
        1. A file, rawData.txt, is used to store the inserted strings. Every time a string is inserted, the string is appended to this file.
        1. Using the string IDs from the node files to retrieve strings from rawData.txt.
        1. The string IDs are in form of \<string-start-position\>-\<string-end-position\>, where the string-start-position refers to the starting position of its associated string in rawData.txt, and similar for the string-end-position.
        1. Using random access to read rawData.txt starting from the string-start-position to the string-end-position to retrieve the string, which gives an "O(1)" time complexity (not absolutely O(1) as the reading speed depends on the persistent storage fragmentation).
1. Check [O1StringSearcher.java][o1-string-searcher-java] for the details.

**The limitations of the O(1)-variation 2**:

1. The node filenames are the node keychains. Consequently, it will not be able to handle the strings with characters invalid to be a part of the filenames.
1. The node filenames are the node keychains. Consequently, it will not be able to handle lengthy strings due to the filepath and filename length limitation from the filesystems, e.g. maximum 255 filename length on EXT4 on Linux.

**The possible solutions to the limitations**:

1. Not directly using the keychains to be the node filenames to resolve the limitation (1).
1. Add a dictionary layer between the nodes and the node files to resolve both limitation (1) & (2).

## The improved synchronization variation

Synchronization:

1. May cocurrently READ the SAME node file.
1. May NOT concurrently WRITE the SAME node file.
1. May NOT concurrently READ and WRITE the SAME node file.

Please note that the [implemented O1 variation](#the-o1-time-complexity-variation) does not allow any concurrent read and write.

Implementation:

1. A read write lock is maintained for each node file.
1. The locks not in use will be removed in a thread-safe way to avoid a memory leakage.
1. Check [ImprovedSyncStringSearcher.java][improved-sync-string-searcher-java] for the details.
    1. **[ImprovedSyncStringSearcher.java][improved-sync-string-searcher-java] is a wrapper class that wraps another [IInstantStringSearcher][i-instant-string-searcher-java] object and on top of it, adds a synchronization mechanism.**


Please note that it is not O(1)-time-complexity but fast enough.

## Extensibility

Please note that this library adopts SOLID principles and it is suggested to extend its feature(s) following SOLID principles.

Moreover, to facilitate the development, a simple JUnit test module is provided. Add your own test cases into the module and run below commands to test your extended library.
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


Forseeable extensions:

1. To solve the the limitation (1) mentioned in [The O(1)-time-complexity variation](#the-o1-time-complexity-variation) in a less costly way:
    1. Create a new class implementing [ITrieNodeAndFilepathMapper.java][i-trie-node-and-filepath-mapper-java] to remap the nodes and the node files.
1. To solve the limitations (1) and (2) mentioned in [The O(1)-time-complexity variation](#the-o1-time-complexity-variation):
    1. Add a dictionary layer between the nodes and the node files.
1. To reuse the synchronization mechanism in [ImprovedSyncStringSearcher][improved-sync-string-searcher-java]:
    ```java
    // Please note that the ImprovedSyncStringSearcher is a wrapper of another IInstantStringSearcher object
    // and on top of the wrapped object, it adds a synchronization mechanism.
    IInstantStringSearcher instantStringSearcher = ImprovedSyncStringSearcher(your_custom_string_searcher_instance);
    ```

## Reminder

Please note that this library is case-sensitive and does not remove the duplicate search result, e.g. it returns two "eye" upon a search of "e". If case-insensitiveness or distinct search result is expected, a post processing of the search result is needed.

## License

[MIT][MIT-license]

[MIT-license]: <LICENSE>
[class-diagram]: <docs/class-diagram.png>
[algo-figure]: <docs/algo.png>
[improved-sync-string-searcher-java]: <instantstringsearcher/github/eightoooeight/instantstringsearcher/ImprovedSyncStringSearcher.java>
[o1-string-searcher-java]: <instantstringsearcher/github/eightoooeight/instantstringsearcher/O1StringSearcher.java>
[i-instant-string-searcher-java]: <instantstringsearcher/github/eightoooeight/instantstringsearcher/IInstantStringSearcher.java>
[i-trie-node-and-filepath-mapper-java]: <instantstringsearcher/github/eightoooeight/instantstringsearcher/trienode/ITrieNodeAndFilepathMapper.java>
