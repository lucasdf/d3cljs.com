(ns d3-cljs.web
  (:require [stasis.core :as stasis]
            [hiccup.page :as hp]
            [shadow.cljs.devtools.api :as shadow]))

(defn get-pages []
  {"/index.html" (hp/html5
                  [:head
                   [:meta {:charset "utf-8"}]
                   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
                   [:title "D3 and CLJS exploration"]]
                  [:body#body
                   [:h1 "D3 and Clojurescript exploration"]
                   [:ul 
                    [:li [:a {:href "/add-circles.html"} "Click and add circles"]]
                    [:li [:a {:href "/pie-charts.html"} "Pie charts"]]
                    [:li [:a {:href "/line-chart.html"} "Line chart"]]]])
   "/pie-charts.html"
   (hp/html5
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      [:title "Pie charts"]]
     [:body#body
      [:a {:href "/index.html"} "Home"]
      [:div#app]
      (hp/include-js "js/main.js")
      [:script "d3_cljs.pie_charts.reload();"]
      [:div [:ul 
             [:li "Click on Reload to reload the charts."]
             [:li "Data is randomly generated using Spec and TestCheck generators."]]]
      [:div [:pre [:code (slurp "./src/d3_cljs/pie_charts.cljs")]]]
      ])

   "/line-chart.html"
   (hp/html5
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      [:title "Line chart"]]
     [:body#body
      [:a {:href "/index.html"} "Home"]
      [:div#app]
      (hp/include-js "js/main.js")
      [:script "d3_cljs.line_chart.reload();"]
      [:div [:ul 
             [:li "Click on Reload to reload the charts."]
             [:li "Data is randomly generated using Spec and TestCheck generators."]
             [:li "Data is generated according to specific distributions."]]]
      [:div [:pre [:code (slurp "./src/d3_cljs/line_chart.cljs")]]]
      ])

   "/add-circles.html" 
   (hp/html5
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      [:title "My page"]]
     [:body#body
      [:a {:href "/index.html"} "Home"]
      [:div#app]
      (hp/include-js "js/main.js")
      [:script "d3_cljs.click_add_circle.reload();"]
      [:div [:ul [:li "Click on the chart to add circles!"]
             [:li "Click on Reload to reload the chart."]
             [:li "Data is randomly generated using Spec and TestCheck generators."]]]
      [:div [:pre [:code (slurp "./src/d3_cljs/click_add_circle.cljs")]]]
      ])
   
   })

(defn export-pages []
  (stasis/export-pages (get-pages) "public/" {}))

(defn export-all []
  (stasis/empty-directory! "public/")
  (stasis/export-pages (get-pages) "public/" {})
  ;(shadow/compile :app)
  (shadow/release :app))

(comment 
  (export-all)
  (export-pages))

