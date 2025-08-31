(ns purchase.utils
  (:require [clojure.tools.logging :as log]
            [clojure.core :as c]
            [clojure.string :as string])
  (:import [java.lang NumberFormatException]))

(defn parse-int
  "Parses a string `s` into an Integer. Returns nil if `s` is nil or parsing fails."
  [s]
  (when s
    (try
      (Integer/parseInt s)
      (catch NumberFormatException _e
        (log/info "The parsing fail or was passed nil" _e)
        nil))))

;(defn parse-boolean
;  "Parse a string `s` into a boolean.
;  Returns true for 'true', 'TRUE', '1', 'yes', 'YES' (case-insensitive).
;  Returns false for any other non-nil string or nil.
;  Returns nil if `s` is nil."
;  [s]
;  (when s
;    (c/contains? #{"true" "TRUE" "1" "yes" "YES"} (string/upper-case s))))
