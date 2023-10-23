package xyz.nietongxue.deploy

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import xyz.nietongxue.common.graph.NoSuchPortException
import xyz.nietongxue.common.graph.WrongConnectionException

/**
 * node by name
 *
 */

class ByNameTest : StringSpec({
    "aps and db" {
        val aps = Service()
        val db = Service()
        val edge = Reference(aps.id, db.id, "db")
        val deployment = Deployment()
        deployment.node(aps)
        deployment.node(db)
        deployment.edge(edge)
    }
    "wrong port" {
        val aps = Service()
        val db = Service()
        val edge = Reference(aps.id, db.id, "cache")
        val deployment = Deployment()
        deployment.node(aps)
        deployment.node(db)
        shouldThrow<NoSuchPortException> {
            deployment.edge(edge)
        }
    }
    "can not connect wrong" {
        val aps = Service()
        val db = Service()
        val edge = Reference(aps.id, db.id, "cache")
        val deployment = Deployment()
        deployment.node(aps)
        deployment.node(db)
        shouldThrow<WrongConnectionException> {
            deployment.edge(edge)
        }
    }
})