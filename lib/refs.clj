; HINT: The lines you need to change are 3,4,11,18, and 34

(def account-a-atom (atom {:checking 100}))
(def account-b-atom (atom {:checking 100}))

(defn balance [acc] (:checking acc))
(defn withdraw [account val]
    "withdraw from the account, return true when successful"
    (if (<= val (:checking @account) )
       (do
         (swap! account assoc :checking (- (:checking @account) val))
         true)
       (do
         (println "Insufficient funds!")
         false)))

(defn deposit [account val]
    (swap! account assoc :checking (+ (:checking @account) val)))

(defn transfer [from to val]
        (Thread/sleep 1000)
        (let [withdrawal-successful? (withdraw from val)]
          (if withdrawal-successful?
              (do
                (deposit to val)
                (println (str "Transfer Successful. From Balance: " (balance @from) ", To Balance: " (balance @to))))
              (println (str "Transfer Unsuccessful. From Balance: " (balance @from) ", To Balance: " (balance @to))))))

(defn loop-over
    "Given an account, withdraw money however many times is specified"
    [num-of-times amount-to-withdraw]
      (doseq [i (range num-of-times)]
        (do
           (future (transfer account-a-atom account-b-atom amount-to-withdraw)))))

(loop-over 10 60)
