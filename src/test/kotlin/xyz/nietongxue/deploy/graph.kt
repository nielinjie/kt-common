package xyz.nietongxue.deploy

import xyz.nietongxue.common.base.Id
import xyz.nietongxue.common.base.Name
import xyz.nietongxue.common.base.v7
import xyz.nietongxue.common.graph.BaseEdge
import xyz.nietongxue.common.graph.BaseGraph
import xyz.nietongxue.common.graph.BaseNode


class Deployment : BaseGraph() {

}

class Service(id: Id) : BaseNode(id) {
    constructor():this(v7())
}

class Reference(from: Id, to: Id, on: Name) : BaseEdge(from, to, on) {
}
