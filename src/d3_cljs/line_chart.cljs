(ns d3-cljs.line-chart
  (:require ["d3" :as d3]
            ["d3-scale-chromatic" :as d3-scale-chromatic]
            ["d3-array" :as d3-array]
            [goog.dom :as dom]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [d3-cljs.d3.core :as d3cljs.core]
            [d3-cljs.d3.color :as d3cljs.color]
            [d3-cljs.reload-button :as reload-button]
            [d3-cljs.generators :as d3cljs.gen]))

(defn line-chart [{:keys [width height margin data data-value-fn svg-id color-schema operations information]
                   :or {data-value-fn :value svg-id "#app" color-schema "schemeCategory10"}}]
  (let [svg (d3cljs.core/svg svg-id
                             {:width width :height height}
                             {:transform (str "translate(" (:left margin) "," (:top margin) ")")})
        width (- width (:left margin) (:right margin))
        height (- height (:top margin) (:bottom margin))
        x (-> d3
              .scaleTime
              (.domain  (-> d3-array (.extent data (fn [d] (:date d)))))
              (.range [0, width]))

        y (-> d3
              .scaleLinear
              (.domain [0, (-> d3 (.max data (fn [d] (:value d))))])
              (.range [height 0]))]
    (d3cljs.core/axis-bottom svg {:transform (str "translate(0," height ")")} x)
    (d3cljs.core/axis-left svg {} y)

    (-> svg
        (.append "path")
        (.datum data)
        (d3cljs.core/attrs {:d (d3cljs.core/line-path d3 x :date y :value)
                            :fill "none"
                            :stroke (js-invoke d3-scale-chromatic color-schema (rand))
                            :stroke-width "1.5px"}))

    (when information
      (-> svg
          (.append "text")
          (d3cljs.core/attrs {:text-anchor "middle" :x (/ width 2) :y (+ height (:top margin)) :dy ".10em"})
          (.text information)))))

(defn line-color [] (str "#" (.toString (.floor js/Math (* (.random js/Math) 16777215)) 16)))
(defn render-line-chart []
  (let [{:keys [gen-fn gen-name]} (rand-nth d3cljs.gen/generators-value-with-date)]
    (line-chart {:width 500
                 :height 400
                 :margin {:left 20 :right 20 :top 30 :bottom 40}
                 :data (gen/generate (gen-fn))
                 :information gen-name
                 :color-schema (gen/generate (s/gen ::d3cljs.color/str-chromatic-sequential))})))

(defn reload []
  (-> (.getElementById js/document "app")
      (dom/removeChildren))
  (render-line-chart)
  (reload-button/reload-button reload))

