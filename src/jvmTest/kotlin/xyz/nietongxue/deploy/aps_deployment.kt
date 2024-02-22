package xyz.nietongxue.deploy

import io.kotest.core.spec.style.StringSpec
import xyz.nietongxue.common.base.Quantifier
import xyz.nietongxue.common.graph.*
import xyz.nietongxue.common.processing.Assemble
import xyz.nietongxue.common.processing.shouldErr
import xyz.nietongxue.common.processing.shouldSuccess

/**
 * typing node by property value
 *
 */

class ByNameTest : StringSpec({
    "aps and db" {
        val aps = Service(serviceType = "aps")
        val assemble = Assemble<Deployment, GraphLogData>(Deployment(), emptyList())
        assemble.apply(AddNode(aps))
        val re = assemble.finish()
        re.shouldSuccess()
    }
    "aps and db with number" {
        val aps = Service(serviceType = "aps")
        val aps2 = Service(serviceType = "aps")
        val rules = listOf(
            NodeQuantifierRule(ServiceTypeSelector(ServiceTypes.aps), Quantifier.One),
        )
        val assemble = Assemble(Deployment(), rules)
        assemble.apply(AddNode(aps))
        val re = assemble.finish()
        re.shouldSuccess()
    }
    "aps and db with number wrong" {
        val aps = Service(serviceType = "aps")
        val aps2 = Service(serviceType = "aps")
        val rules = listOf(
            NodeQuantifierRule<Deployment>(ServiceTypeSelector(ServiceTypes.aps), Quantifier.One)
        )
        val assemble = Assemble(Deployment(), rules)
        assemble.apply(AddNode(aps))
        assemble.apply(AddNode(aps2))
        val re = assemble.finish()
        re.shouldErr(StringLogData("NodeQuantifierRule failed"))
    }

    "wrong port" {
        val aps = Service(serviceType = "aps")
        val cache = Service(serviceType = "cache")
        val db = Service(serviceType = "db")
        val assemble = DeploymentAssemble()
        assemble.apply(AddNode(aps))
        assemble.apply(AddNode(cache))
        assemble.apply(AddNode(db))
        assemble.apply(Connect(Reference(aps.id, "cache", db.id)))
        assemble.finish().shouldErr(StringLogData("NodePortRule failed"))
    }
    "can not connect to wrong target" {
        val aps = Service(serviceType = "aps")
        val cache = Service(serviceType = "cache")
        val db = Service(serviceType = "db")
        val assemble = DeploymentAssemble()
        assemble.apply(AddNode(aps))
        assemble.apply(AddNode(cache))
        assemble.apply(AddNode(db))
        assemble.apply(Connect(Reference(aps.id, "db", cache.id)))
        assemble.finish().shouldErr(StringLogData("ConnectionTargetRule failed"))
    }

})