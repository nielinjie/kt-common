package xyz.nietongxue.deploy

import xyz.nietongxue.common.base.Id
import xyz.nietongxue.common.base.Name
import xyz.nietongxue.common.base.v7
import xyz.nietongxue.common.graph.*
import xyz.nietongxue.common.graph.Quantifier.One
import xyz.nietongxue.common.processing.Assemble


class Deployment : BaseGraph()

class Service(override val id: Id = v7(), val serviceType: String) : BaseNode(id)

class Reference(from: Id, on: Name, to: Id) : BaseEdge(from, on, to)


data class ServiceTypeSelector(val type: String) : NodeSelector<Deployment> {
    override fun select(input: List<Node>, context: Deployment): List<Node> {
        return input.filter {
            when (it) {
                is Service -> it.serviceType == type
                else -> false
            }
        }
    }
}

class ServiceTypeQualityRule(type: String, quantifier: Quantifier) :
    NodeQuantifierRule<Deployment>(ServiceTypeSelector(type), quantifier)


class DeploymentAssemble : Assemble<Deployment, GraphLogData>(
    Deployment(), listOf(
        ServiceTypeQualityRule("aps", One),
        ServiceTypeQualityRule("db", One),
        ServiceTypeQualityRule("cache", One),
        NodePortRule(ServiceTypeSelector("aps"), listOf("db")),
        ConnectionTargetRule(ServiceTypeSelector("aps"), "db", ServiceTypeSelector("db")),
        ConnectionQualityRule(ServiceTypeSelector("aps"), "db", One)
    )
)
