(ns d3-cljs.pie-charts
  (:require
   ["d3-scale-chromatic" :as d3-scale-chromatic]
   [goog.dom :as dom]
   [clojure.spec.alpha :as s]
   [clojure.test.check.generators :as gen]
   [d3-cljs.d3.core :as d3cljs.core]
   [d3-cljs.d3.color :as d3cljs.color]
   [d3-cljs.reload-button :as reload-button]
   [d3-cljs.generators :as d3cljs.gen]))

(s/def ::width (s/and number? pos?))
(s/def ::heigth (s/and number? pos?))

(defn pie-basic [{:keys [width height margin data data-value-fn svg-id color-schema operations]
                  :or {data-value-fn :value svg-id "#app" color-schema "schemeCategory10"}}]
  "https://www.d3-graph-gallery.com/graph/pie_basic.html"
  (let [radius (- (/ (Math/min width height) 2) margin)
        svg (d3cljs.core/svg svg-id
                             {:width width :height height}
                             {:transform (str "translate(" (/ width 2) "," (/ height 2) ")")})
        color (d3cljs.color/category data (aget d3-scale-chromatic color-schema))]
    (->
     (d3cljs.core/select-all svg "whathever")
     (d3cljs.core/pie-data data data-value-fn)
     .enter
     (.append "path")
     (d3cljs.core/attrs {:d (d3cljs.core/arc 0 radius)
                         :fill (fn [d]
                                 (color (. d -data)))
                         :stroke "black"
                         :stroke-width "0.4px"
                         :opacity 0.7}))

    (loop [e svg
           ops operations]
      (let [[op & more] (first ops)]
        (when op
          (recur (case op
                   :select-all (d3cljs.core/select-all e (first more))
                   :data (d3cljs.core/pie-data e data data-value-fn)
                   :enter (.enter e)
                   :append (.append e (first more))
                   :text (.text e (first more))
                   :transform (.attr e "transform"
                                     (fn [d] (str "translate(" (-> (d3cljs.core/arc 0 radius) (.centroid d)) ")")))
                   :style (.style e (first more) (second more)))
                 (rest ops)))))))

(def width 300)
(def height 300)
(def margin 30)
(def radius (- (/ (Math/min width height) 2) margin))
(def annotations [[:select-all "categories"] [:data] [:enter] [:append "text"]
                  [:text (fn [d]
                           (str "grp " (-> d .-index)))]
                  [:transform (fn [d] (str (str "translate(" (-> (d3cljs.core/arc 0 radius) (.centroid d)) ")")))]
                  [:style "text-anchor" "middle"]
                  [:style "font-size" 12]])

(defn render-pie-basic []
  (pie-basic {:width width #_(gen/generate (gen/choose 100 500))
              :height height #_(gen/generate (gen/choose 100 500))
              :margin margin
              :data (gen/generate (gen/vector d3cljs.gen/random-values-generator 1 50))
              :color-schema (gen/generate (s/gen ::d3cljs.color/str-chromatic-categories))}))

(defn render-pie-annotations []
  (pie-basic {:width width #_(gen/generate (gen/choose 100 500))
              :height height #_(gen/generate (gen/choose 100 500))
              :margin margin
              :data (gen/generate (gen/vector d3cljs.gen/random-values-generator 1 6))
              :color-schema (gen/generate (s/gen ::d3cljs.color/str-chromatic-categories))
              :operations annotations}))

(defn ^:export reload []
  (-> (.getElementById js/document "app")
      (dom/removeChildren))
  (render-pie-basic)
  (render-pie-annotations)
  (reload-button/reload-button reload))

