## Atoms

> **Atom:** A datatype that is indivisible and unchanging. A process generates a new atom and we choose to associate the identity with the new atom, which keeps us from mutating the data.

### State and Value in Clojure

In our previous problem we were disobeying our rules for thread safety by sharing mutable state between threads. We ended up locking up the shared data when it was in use in order to prevent this.  This should remind us of rule #3 from our rules for the safest path to concurrency.

> `If you must share data across threads, don't share mutable data.`

Here is where we get to talk about Clojure's bread and butter: immutable state. The problem that we have encountered with futures is the fact that we have multiple threads accessing some data that can be in an unknown state. Clojure takes care of this with a reference type known as the atom.

It may be somewhat difficult to tell the difference between your data's state and its value in your code.  Lets explore this idea with an example.

Think of a person as an example, let's say YOU!  You have existed in many different states throughout your lifetime.  You were once an infant, child, teenager, and now an adult.  But remember, you have never existed in multiple states at once.  For example, you cannot be both an infant as well as an adult.  

Your state of being a child allowed you to possess certain characteristics at that point in time.  When you transitioned into adulthood, you took on different characteristics at that point in time.

However, you also have some continuous identity that you used to represent all of those states over the course of your lifetime.  That's the value that represents YOU and all of those changes throughout your life.

> **Value:** Some identity that represents your data throughout time.

> **State:** Characteristics that your data can possess at some point in time. (Values which change over time)

If we wanted to represent this concept of separate values and identities in Clojure, Clojure's goal is to have some identity that represents you; not you at any particular point in time, but you as a logical entity throughout time.  In Clojure we can easily ensure that a person is never caught in between two states of identity with an atomic reference type.

### Using an Atom

Atoms are the most basic reference type; they are identities that implement synchronous, uncoordinated, atomic compare-and-set modification.  An Atom is an immutable piece of data represented by some state, and as soon as we change our atom we switch out its old state for a new state representing its updates.

Creating an atom that contains a reference to a map:

![Alt text](/img/atom-value-state.jpg)

~~~clojure
   (def checking-account (atom {:checking 100 :name "My-Account"}))
~~~

Accessing the value of our atom, we need to dereference it:

~~~clojure
   @checking-account # => {:checking 100, :name "My-Account"}
~~~

If we want to "change" the hash that our atom references, we use the function `swap!`.  `swap!` will only work on an atom.  The first argument is the atom and the second argument takes a function that is to be applied to the value the atom references.

![Alt text](/img/atom-value-state-2.jpg)

~~~clojure
   (swap! checking-account assoc :name "NEW NAME")
~~~

`swap!` uses the `compare-and-set` property we mentioned earlier.  Let's elaborate a bit more on what `compare-and-set modification` means using the example of our checking account.  When we're modifying our checking account balance by withdrawing $1, our atom should look to ensure our current balance is an appropriate amount to withdraw from. Next we withdraw the amount and leave all of our unchanged information associated with our checking account.  This gets associated with our new state that represents our atom's value, including our new changes.  Next we compare our known original known state to the actual current state just before we update. If that has changed in another thread we throw out all of our work and start over again with the correct state.  If you fire up multiple threads to change the same atom, this could possibly cause MULTIPLE collisions leaving swap to attempt multiple tries before finally updating the change.

***

#### Programming Exercise

Let's complete our example `lib/atoms.clj`. We want to show two things about our new knowledge of atoms. Show an example that demonstrates an atom's synchronousness properties. For example, lets print a message each time we call `withdraw` to show that it will block until completion. Next lets demonstrate the `compare-and-set` property. If we have multiple threads attempting to withdraw amounts at the same time, that is sure to cause some collisions.

> _TIP: skip ahead to an implementation of using delays with `git checkout atoms-solved`_

Continue on to [Agents](Agents.md).
