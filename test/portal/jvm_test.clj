(ns portal.jvm-test
  (:require [clojure.test :refer [deftest is]]
            [portal.api :as p]
            [portal.runtime.index :as index]
            [portal.runtime.jvm.client :as client]
            [portal.runtime.jvm.launcher :as launcher]))

(defn- headless-chrome-flags [url]
  ["--headless" "--disable-gpu" url])

(defn- open [f]
  (with-redefs [launcher/chrome-flags f] (p/open)))

(deftest e2e-jvm
  (reset! index/testing? true)
  (when-let [portal (open headless-chrome-flags)]
    (with-redefs [client/timeout 60000]
      (reset! portal 0)
      (is (= @portal 0))
      (swap! portal inc)
      (is (= @portal 1))))
  (p/close))

