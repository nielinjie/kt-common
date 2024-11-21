package xyz.nietongxue.common.jpath

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class JsonPathTest {

    @Test
    fun testToken() {
        val jsonPath = JsonPath.compile("$.children[0].name")
        val allPath = jsonPath.path
        println(allPath)
    }

    @Test
    fun parseToJPath() {
        val path = "\$['children'][0]['name']"
        JPath.parse(JsonPath.compile(path)).also {
            it.toJsonPath().also {
                assertThat(it).isEqualTo(path)
            }
        }
    }

    @Test
    fun setByJPath() {
        val json = """
            {
                "children": [
                    {
                        "name": "Tom"
                    },
                    {
                        "name": "Jerry"
                    }
                ]
            }
        """.trimIndent()
        val jpath = JPath.parse(JsonPath.compile("$.children[0].name"))
        val path = jpath.toJsonPath()
        JsonPath.parse(json).read<Any>(path).also {
            assertThat((it as TextNode).asText()).isEqualTo("Tom")
        }
        val value = "Jack"
        val result = JsonPath.parse(json).set(jpath.toJsonPath(), value)
        println(result.jsonString())
    }


    @ParameterizedTest
    @CsvSource(
        "$.mate",
        "$.mate.name",
        "$.children[0].name",
        "$.children[4].name",
    )
    fun stepByJPath(path: String) {
        val json = """
            {
                
            }
        """.trimIndent()
        val doc = JsonPath.parse(json)
        val jpath = JPath.parse(JsonPath.compile(path))
        val value = "Jack"
        jpath.set(doc, value)

        val string: String = jpath.read(doc, String::class.java)
        assertThat(string).isEqualTo(value)
    }


    @Test
    fun multiSet() {
        val om = jacksonObjectMapper()
        val json = om.createObjectNode()
        val jpath = JPath.parse(JsonPath.compile("$.children[2].name"))
        val jpath2 = JPath.parse(JsonPath.compile("$.children[5].name"))
        val value = "Jack"
        val value2 = "Tom"
        val json2 = jpath.set(json, value)
        val json3 = jpath2.set(json2, value2)
        json2.read(jpath, String::class.java).also {
            assertThat(it).isEqualTo(value)
        }
        json3.read(jpath2, String::class.java).also {
            assertThat(it).isEqualTo(value2)
        }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            JPath.useDefaultConfig()
        }
    }

    @Test
    fun nomalize() {
        val path = "$.children[*].name"
        val json = """
            {
                "children": [
                    {
                        "name": "Tom"
                    },
                    {
                        "name": "Jerry"
                    }
                ]
            }
        """.trimIndent()
        val jpaths = (jacksonObjectMapper().readTree(json) as ObjectNode).concretePaths(listOf(JsonPath.compile(path)))
        jpaths.also {
            it.onEach {
                println(it.toJsonPath())
            }
        }
    }
}