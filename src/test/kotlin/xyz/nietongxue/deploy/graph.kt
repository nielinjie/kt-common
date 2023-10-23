package xyz.nietongxue.deploy

import xyz.nietongxue.common.graph.*


class Deployment : BaseGraph() {

}

class Service(id: Id) : BaseNode(id) {
    constructor():this(v7())
}

class Reference(from: Id, to: Id, on: Name) : BaseEdge(from, to, on) {
}
