# Winter 2019: Homework 2: Part 1
## Part 1a: Implement DoubleLinkedList
**Task: complete the DoubleLinkedList class.**

A doubly-linked list is a similar to the singly-linked lists you studied in CSE 143 except in two crucial ways: your nodes now have pointers to both the previous and next nodes, and your linked list class has now have pointers to both the front and back of your sequence of list node objects.
![Linked List](https://github.com/tungfang/Data-Structures-and-Algorithms/blob/master/373_images/HW2_Part1_images/linkedList.png)
Doubly-linked lists containing the same data will look like this:
![Double Linked List](https://github.com/tungfang/Data-Structures-and-Algorithms/blob/master/373_images/HW2_Part1_images/doubleLinkedList.png)
Your implementation should:

1. Be generic (e.g. you use generics to let the users store objects of any types in your list)
2. Implement the **IList** interface. This means you will be using this file extensively as a template for what your code will do.
3. Be as asymptotically efficient as possible.
4. Contain exactly as many node objects as there are items in the list. (If the user inserts 5 items, you should have 5 nodes in your list).

## Part 1b: Write missing tests
**Task: Modify TestDeleteFunctionality, and TestDeleteStress and add in tests for the delete method.**

In part 1b, you will practice writing some unit tests using JUnit.

Start by skimming through **TestDoubleLinkedList.java** and familiarize yourself with the tests we have given you. Since this is the first assignment, we've given you most of the tests you need, except for a few. Can you see what tests are missing?

There are no tests for the **DoubleLinkedList.delete(...)** method! Your job here is to write tests for this method.

A few additional notes:
- To help facilitate grading, we ask that you split your tests into two groups. All tests that check and make sure your **delete(...)** behaves correctly and matches the IList specification should be placed within the **TestDeleteFunctionality** file.

Please keep all tests within **TestDeleteFunctionality** short. Every test you add to this file should have a timeout of a second or less. (You're encouraged to add as many tests as you want, however.)

Add any stress tests to **TestDeleteStress**. Stress tests are tests that either (a) focus on testing to make sure your code is efficient or (b) focus on testing to make sure your code is correct when it's asked to handle large amounts of data.

Please see the existing stress tests in **TestDoubleLinkedList** to get a sense of what sorts of timeouts and list sizes you should be using.
- Your stress tests do not need to be particularly complicated. We'll be assessing your tests by copying them into many different projects that contain deliberately buggy implementations of **delete(...)**. Most of these implementations will contain actual bugs that can be caught by your **TestDeleteFunctionality** tests; only one or two of them will be functionally correct yet grossly inefficient.

## Part 1c: Implement ArrayDictionary
**Task: complete the ArrayDictionary class.**
Your **ArrayDictionary** class will internally keep track of its key-value pairs by using an array containing **Pair** objects.
![code0](https://github.com/tungfang/Data-Structures-and-Algorithms/blob/master/373_images/HW2_Part1_images/code0.PNG)
Your internal array should look like the following:
![arrayDict](https://github.com/tungfang/Data-Structures-and-Algorithms/blob/master/373_images/HW2_Part1_images/arrayDictionary.png)

