# O(1) String Searcher Library

O(1) String Searcher provides a data structure which grants an **O(1)-time-complexity string search** but with an **O(n^2)-time-complexity string insertion**. One of its good fit scenarios is the chat history search function.

## Table of content

1. [Build library](#build-library)
1. [Library Usage](#library-usage)
1. [Alogrithm](#alogrithm)
1. [Class diagram](#class-diagram)
1. [Limitations and solutions](#limitations-and-solutions)
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
1. Run the Junit to test the library.
    ```bash
    java --module-path build:junittest/dependencies -m junittest/github.eightoooeight.instantstringsearcher.junittest.TestRunner
    ```

## Library Usage

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

## Alogrithm

The alogrithm: 

1. The strings to be inserted is saved into a trie tree.
  ![Triel formation figure][trie-formation-figure]
1. The node with the keychain as same as the searching words would be bound with the search result's string IDs, e.g. when the user is searching for "BC", the node with the keychain, "BC", would have the string IDs of the search result.
1. Every node is assigned a separate file to store its data.
1. An O(1) method is provided to locate the node file with a specified keychain.
1. May only load the node file that is responsible for the search result, i.e. no need to load the whole trie.

Consider that this string searcher is particularly useful only when the string amount is too massive, this library is designed to save the trie into the persistent storage and to be able to load only a specified node in response to the string search, so that the RAM/swapping will not be overwhelmed and a quick node loading can be guaranteed.

## Class diagram

![Class diagram][class-diagram]

## Limitations and solutions

Limitations:

1. In this library, the node file names are the node keychains. Consequently, it will not be able to handle the strings with characters invalid to be a part of the filenames.
1. In this library, the node file names are the node keychains. Consequently, it will not be able to handle lengthy strings due to the filepath and filename length limitation from the filesystems, e.g. maximum 255 filename length on EXT4 on Linux.

Solutions:

1. Modify the TrieNodeAndFilepathMapper class to resolve the invalid filename character problem.
1. Add a dictionary layer between the nodes and the files to cope with the above 2 limitations.

## Reminder

Please note that this library is case-sensitive and does not remove the duplicate search result, e.g. it returns two "eye" upon a search of "e". If case-insensitiveness or distinct search result is expected, a post processing of the search result is needed.

## License

[MIT][MIT-license]

[MIT-license]: LICENSE
[class-diagram]: docs/class-diagram.png
[trie-formation-figure]: docs/trie-formation.png
