(ns d3-cljs.click-add-circle
  (:require
   [d3-cljs.d3.core :as d3cljs.core]
   [goog.dom :as dom]
   [clojure.spec.alpha :as s]
   ["d3" :as d3]
   [d3-cljs.d3.color :as d3cljs.color]
   [clojure.test.check.generators :as gen]
   [d3-cljs.reload-button :as reload-button]))

(defn circle-chart [{:keys [width height margin data data-value-fn svg-id color-schema operations information]
                     :or {data-value-fn :value svg-id "#app" color-schema "schemeCategory10"}}]
  "http://bl.ocks.org/WilliamQLiu/76ae20060e19bf42d774"
  "see also https://jsfiddle.net/GordyD/0o71rhug/1/"
  (let [svg (d3cljs.core/svg svg-id
                             {:width width :height height}
                             {})
        x-scale (d3cljs.core/linear-scale
                 [0 (+ (apply max (map :x data)) 10)]
                 [(:left margin) (- width (:right margin))])
        y-scale (d3cljs.core/linear-scale
                 [0 (+ (apply max (map :y data)) 10)]
                 [(:top margin), (- height (:bottom margin))])]

    (d3cljs.core/axis-left svg {:class "axis" :transform (str "translate(" (:left margin) "," 0 ")")} y-scale)
    (d3cljs.core/axis-top svg {:class "axis" :transform (str "translate(" 0 "," (:top margin) ")")} x-scale)
    (-> (d3cljs.core/select-all svg "circle")
        (.data data)
        .enter
        (.append "circle")
        (d3cljs.core/attrs {:cx (fn [d] (x-scale (:x d)))
                            :cy (fn [d] (y-scale (:y d)))
                            :r 6}))
    (-> (d3cljs.core/select "svg")
        (.on "click" (fn [event]
                       (let [selection (d3cljs.core/select-all svg "circle")
                             data (js->clj (.data selection))
                             [x y] (-> d3 (.pointer event))]
                         (-> selection
                           ;; invert goes from pixel (range) to data (domain)
                             (.data (conj data {:x (-> x-scale (.invert x))
                                                :y (-> y-scale (.invert y))}))
                             .enter
                             (.append "circle")
                             (d3cljs.core/attrs {:cx (fn [d] (x-scale (:x d)))
                                                 :cy (fn [d] (y-scale (:y d)))
                                                 :r 6}))))))))
(defn render-click-add-circle []
  (circle-chart
   {:width 500
    :height 400
    :margin {:left 25 :right 20 :top 30 :bottom 40}
    :data (gen/generate
           (gen/vector
            (gen/let [x (gen/choose 0 500)
                      y (gen/choose 0 400)]
              (gen/return {:x x :y y}))
            (gen/generate (gen/choose 1 50))))
    :color-schema (gen/generate (s/gen ::d3cljs.color/str-chromatic-sequential))}))

(defn ^:export reload []
  (-> (.getElementById js/document "app")
      (dom/removeChildren))
  (render-click-add-circle)
  (reload-button/reload-button reload))
