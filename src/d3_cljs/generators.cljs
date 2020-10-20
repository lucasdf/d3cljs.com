(ns d3-cljs.generators
  (:require
   ["luxon" :as luxon]
   ["jstat" :as jstat]
   [clojure.test.check.generators :as gen]))

(def value-generator (gen/choose 0 100))
(def random-values-generator (gen/fmap (fn [v] {:value v}) value-generator))

(defn date-generator []
  (gen/let [year (gen/choose 1971 2020)
            month (gen/choose 1 12)]
    (-> luxon/DateTime (.utc year month))))
(defn make-date []
  (gen/generate (date-generator)))

;; make this infinite
(defn sequence-of-months-generator
  ([initial-date]
   (gen/let [months (gen/choose 2 30)]
     (sequence-of-months-generator initial-date months)))
  ([initial-date months]
   (gen/return (->> (range months) (map #(-> initial-date (.plus (clj->js {:months %})))) (take months)))))

(defn make-sequence-of-months
  ([initial-date]
   (gen/generate (sequence-of-months-generator initial-date)))
  ([initial-date months]
   (gen/generate (sequence-of-months-generator initial-date months))))

(defn poisson-sample-generator
  ([scale]
   (gen/let [lambda (gen/double* {:min 0.1 :max 10 :NaN? false})]
     (poisson-sample-generator lambda)))
  ([scale lambda]
   (gen/return (* scale (.sample jstat/poisson lambda)))))
(defn make-poisson-sample [scale]
  (gen/generate (poisson-sample-generator scale)))
(defn random-poisson-samples-generator []
  (gen/let [samples (gen/choose 2 100)
            scale (gen/choose 1 10)
            lambda (gen/double* {:min 0.1 :max 10 :NaN? false})]
    (gen/return (map #(gen/generate (poisson-sample-generator scale lambda)) (range samples)))))
(defn make-random-poisson-samples []
  (gen/generate (random-poisson-samples-generator)))
(defn random-poisson-values-with-date []
  (gen/let [values (random-poisson-samples-generator)
            initial-date (date-generator)
            months (sequence-of-months-generator initial-date (count values))]
    (map (fn [v m] {:value v :date m}) values months)))

(defn beta-sample-generator
  [alpha beta scale]
  (gen/return (* scale (.sample jstat/beta alpha beta))))
(defn make-beta-sample [scale]
  (let [alpha (gen/generate (gen/double* {:min 0.2 :max 10 :NaN? false}))
        beta (gen/generate (gen/double* {:min 0.2 :max 10 :NaN? false}))]
    (gen/generate (beta-sample-generator alpha beta scale))))
(defn random-beta-samples-generator []
  (gen/let [samples (gen/choose 2 100)
            scale (gen/choose 1 10)
            alpha (gen/double* {:min 0.5 :max 10 :NaN? false})
            beta (gen/double* {:min 0.5 :max 10 :NaN? false})]
    (gen/return (map #(gen/generate (beta-sample-generator alpha beta scale)) (range samples)))))
(defn make-random-beta-samples []
  (gen/generate (random-beta-samples-generator)))
(defn random-beta-values-with-date []
  (gen/let [values (random-beta-samples-generator)
            initial-date (date-generator)
            months (sequence-of-months-generator initial-date (count values))]
    (map (fn [v m] {:value v :date m}) values months)))

(def generators-value-with-date [{:gen-fn random-poisson-values-with-date
                                  :gen-name :poisson}
                                 {:gen-fn random-beta-values-with-date
                                  :gen-name :beta}])

(comment
  (make-poisson-sample 10)
  (make-random-poisson-samples)
  (make-sequence-of-months (make-date))
  (make-sequence-of-months (make-date) (gen/generate (gen/choose 1 10))))
