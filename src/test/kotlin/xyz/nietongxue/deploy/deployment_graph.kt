package xyz.nietongxue.deploy

import xyz.nietongxue.common.base.*
import xyz.nietongxue.common.graph.*
import xyz.nietongxue.common.graph.selectors.NodeSelector
import xyz.nietongxue.common.processing.Assemble
import xyz.nietongxue.deploy.ServiceTypes.aps
import xyz.nietongxue.deploy.ServiceTypes.cache
import xyz.nietongxue.deploy.ServiceTypes.db


class Deployment : BaseGraph()

class Service(override val id: Id = v7(), val serviceType: String) : BaseNode(id)

class Reference(from: Id, on: Name, to: Id) : BaseEdge(from, on, to)


object ServiceTypes : Types<Service> {
    val aps = object : Type<Service> {
        override val name: Path = Path.fromString("aps")
        override fun instanceOf(value: Service): Boolean {
            return value.serviceType == "aps"
        }
    }
    val db = object : Type<Service> {
        override val name: Path = Path.fromString("db")
        override fun instanceOf(value: Service): Boolean {
            return value.serviceType == "db"
        }
    }
    val cache = object : Type<Service> {
        override val name: Path = Path.fromString("cache")
        override fun instanceOf(value: Service): Boolean {
            return value.serviceType == "cache"
        }
    }
    override val types: List<Type<Service>> = listOf(
        aps, db, cache
    )
}

data class ServiceTypeSelector(val type: Type<Service>) : NodeSelector<Deployment> {
    override fun select(input: List<Node>, context: Deployment): List<Node> {
        return input.filter {
            when (it) {
                is Service -> type.instanceOf(it)
                else -> false
            }
        }
    }
}

class ServiceTypeQualityRule(type: Type<Service>, quantifier: Quantifier) :
    NodeQuantifierRule<Deployment>(ServiceTypeSelector(type), quantifier)


class DeploymentAssemble : Assemble<Deployment, GraphLogData>(
    Deployment(), listOf(
        ServiceTypeQualityRule(aps, Quantifier.One),
        ServiceTypeQualityRule(db, Quantifier.One),
        ServiceTypeQualityRule(cache, Quantifier.One),
        NodePortRule(ServiceTypeSelector(aps), listOf("db")),
        ConnectionTargetRule(ServiceTypeSelector(aps), "db", ServiceTypeSelector(db)),
        ConnectionQualityRule(ServiceTypeSelector(aps), "db", Quantifier.One)
    )
)
