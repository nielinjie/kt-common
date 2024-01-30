package xyz.nietongxue.common.base

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class StringTest:StringSpec ({
    "minus at end" {
        "aaabb".minusAtEnd("bb").shouldBe("aaa")
        "aaabb".minusAtEnd("b").shouldBe("aaab")
        "aaabb".endBy("bb").shouldBe("aaa")
        "aaabb".endBy("b").shouldBe("aaab")
        "aaabb".endBy("bbb").shouldBeNull()
    }
    "minus at start"{
        "aaabb".minusAtStart("aa").shouldBe("abb")
        "aabbb".minusAtStart("aa").shouldBe("bbb")
        "aaabb".startBy("aa").shouldBe("abb")
        "aabbb".startBy("aa").shouldBe("bbb")
        "abbb".startBy("aa").shouldBeNull()
    }
    "around"{
        "aaabb".aroundBy("aa","bb").shouldBe("a")
        "aaabb".aroundBy("aa","b").shouldBe("ab")
        "aaabb".aroundBy("a","bb").shouldBe("aa")
        "aaabb".aroundBy("a","b").shouldBe("aab")
        "aaabb".aroundBy("aa","bbb").shouldBeNull()
        "aaabb".aroundBy("aaa","bb").shouldBe("")
    }
    "cases"{
        "aaBbCc".lowerUnderscore().shouldBe("aa_bb_cc")
        "aa_bb_cc".lowerUnderscore().shouldBe("aa_bb_cc")
        "aa_bb_cc dd ee".lowerUnderscore().shouldBe("aa_bb_cc_dd_ee")
    }
})