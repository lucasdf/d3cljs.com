(ns d3-cljs.server
  (:require [d3-cljs.pie-charts]
            [d3-cljs.line-chart]
            [d3-cljs.click-add-circle]))

(defn ^:dev/after-load shadow-reload []
  (-> js/document .-location .reload))
