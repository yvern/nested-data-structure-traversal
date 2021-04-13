(def input
  [{:title "Getting started"
    :reset-lesson-position false
    :lessons [{:name "Welcome"}
              {:name "Installation"}]}

   {:title "Basic operator"
    :reset-lesson-position false
    :lessons [{:name "Addition / Subtraction"}
              {:name "Multiplication / Division"}]}

   {:title "Advanced topics"
    :reset-lesson-position true
    :lessons [{:name "Mutability"}
              {:name "Immutability"}]}])

(def expected
  [{:title "Getting started"
    :reset-lesson-position false
    :lessons [{:name "Welcome" :position 1}
              {:name "Installation" :position 2}]
    :position 1}
   {:title "Basic operator"
    :reset-lesson-position false
    :lessons [{:name "Addition / Subtraction" :position 3}
              {:name "Multiplication / Division" :position 4}]
    :position 2}
   {:title "Advanced topics"
    :reset-lesson-position true
    :lessons [{:name "Mutability" :position 1}
              {:name "Immutability" :position 2}]
    :position 3}])

(defn position
  ([offset] #(position (+ offset %1) %2))
  ([idx coll] (assoc coll :position (inc idx))))

(defn update-lessons [lessons offset]
  (into [] (map-indexed (position offset)) lessons))

(defn update-section [offset section]
  (update section :lessons update-lessons offset))

(defn get-offset
  [reset-lesson-position {:keys [lessons]}]
  (if reset-lesson-position 0
      (or (some-> lessons peek :position) 0)))

(defn combine-section [last-section section]
  (-> (:reset-lesson-position section)
      (get-offset last-section)
      (update-section section)))

(defn update-sections [data]
  (->> data
       (reductions combine-section {})
       (into [] (comp (drop 1) (map-indexed position)))))

(assert (= expected (update-sections input)))