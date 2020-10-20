(defproject d3-cljs "0.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.764"]
                 [thheller/shadow-cljs "2.11.4"
                  :exclusions [[com.google.javascript/closure-compiler-unshaded]
                               [org.clojure/data.json]
                               [org.clojure/tools.reader]]]
                 [reagent "1.0.0-alpha2"]
                 [rid3 "0.2.2"]
                 [org.clojure/test.check "0.9.0"]
                 [stasis "2.5.0"]
                 [hiccup "1.0.5"]]
  :source-paths ["src"])
