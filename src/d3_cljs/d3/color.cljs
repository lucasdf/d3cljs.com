(ns d3-cljs.d3.color
  (:require ["d3" :as d3]
            [clojure.spec.alpha :as s]
            ))
;; https://github.com/d3/d3-scale-chromatic

(s/def ::chromatic-categories #{:category10 :accent :dark2 :paired :pastel1 :pastel2 :set1 :set2 :set3 :tableau10})
(s/def ::str-chromatic-categories #{"schemeCategory10" "schemeAccent" "schemeDark2" "schemePaired" "schemePastel1" "schemePastel2" "schemeSet1" "schemeSet2" "schemeSet3" "schemeTableau10"})

(defn category [domain ranges]
  (-> d3
      .scaleOrdinal
      (.domain domain)
      (.range ranges)))

(s/def ::str-chromatic-sequential
  #{"interpolateTurbo" "interpolateViridis" "interpolateInferno" "interpolateMagma" "interpolatePlasma" "interpolateCividis" "interpolateWarm" "interpolateCool" "interpolateCubehelixDefault" "interpolateBuGn" "interpolateBuPu" "interpolateGnBu" "interpolateOrRd" "interpolatePuBuGn" "interpolatePuBu" "interpolatePuRd" "interpolateRdPu" "interpolateYlGnBu" "interpolateYlGn" "interpolateYlOrBr" "interpolateYlOrRd"})
