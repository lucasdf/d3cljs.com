(ns d3-cljs.d3.core
  (:require ["d3" :as d3]))

(defn generate-attrs [elem attrs]
  (map (fn [[k v]]
         '(.attr elem (name k) v))
       (seq attrs)))

;(generate-attrs 'e {:a "b" :c :d})


(defn attrs [elem attrs]
  (doall (map (fn [[k v]]
                (.attr elem (name k) v))
              (seq attrs)))
  elem)

(defn select
  ([e]
   (.select d3 e))
  ([e selector]
   (.select e selector)))

(defn select-all
  ([e]
   (.selectAll d3 e))
  ([e selector]
   (.selectAll e selector)))

(defn pie-data [elem data value-fn]
  (.data elem ((-> d3 .pie (.value #(value-fn %))) data)))

(defn line-path [elem x-fn x-key y-fn y-key]
  (-> elem
      .line
      (.x (fn [d]
            (x-fn
             (x-key d))))
      (.y (fn [d] (y-fn (y-key d))))))

(defn arc [inner outer]
  (-> d3 .arc (.innerRadius inner) (.outerRadius outer)))

(defn axis-bottom [elem attributes x]
  (-> elem (.append "g") (attrs attributes) (.call (.axisBottom d3 x))))

(defn axis-left [elem attributes y]
  (-> elem (.append "g") (attrs attributes) (.call (.axisLeft d3 y))))

(defn axis-top [elem attributes x]
  (-> elem (.append "g") (attrs attributes) (.call (.axisTop d3 x))))

(defn svg [id svg-attrs g-attrs]
  (->
   (select id)
   (.append "svg")
   (attrs svg-attrs)
   (.append "g")
   (attrs g-attrs)))

(defn linear-scale [scale-domain scale-range]
  (-> d3 .scaleLinear (.domain scale-domain) (.range scale-range)))
