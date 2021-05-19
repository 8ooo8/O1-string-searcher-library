# O(1) String Searcher

O(1) String Searcher provides a data structure which grants an **O(1)-time-complexity string search** but with an **O(n^2)-time-complexity string insertion**. One of its good fit scenarios is the chat history search function.

## Alogrithm

The alogrithm: 

1. The strings to be inserted is saved into a trie tree.
  ![Triel formation figure][trie-formation-figure]
1. The node with the keychain as same as the searching words would be bound with the search result's string IDs, e.g. when the user is searching for "BC", the node with the keychain, "BC", would have the string IDs of the search result.
1. Every node is assigned a separate file to store its data.
1. An O(1) method is provided to locate the node file with a specified keychain.
1. May only load the node file that is responsible for the search result, i.e. no need to load the whole trie.

Consider that this string searcher is particularly useful only when the string amount is too massive, this library is designed to save the trie into the persistent storage and to be able to load only a specified node in response to the string search. Therefore, the RAM/swapping will not be overwhelmed and a quick node loading can be guaranteed.

## Class diagram

![Class diagram][class-diagram]

## Limitation and possible improvement

Limitation:

1. In this library, the node file names are the node keychains. Consequently, it will not be able to handle the strings with characters invalid to be a part of the filenames.
1. In this library, the node file names are the node keychains. Consequently, it will not be able to handle lengthy strings due to the filepath and filename length limitation from the filesystems, e.g. maximum 255 filename length on EXT4 on Linux.

Possible improvement:

1. Modify the TrieNodeAndFilepathMapper class to resolve the invalid filename character problem.
1. On the top of the library, add a layer to deal with the above 2 limitations.

## License

[MIT][MIT-license]

[MIT-license]: LICENSE
[class-diagram]: doc/class-diagram.png
[trie-formation-figure]: doc/trie-formation.png
