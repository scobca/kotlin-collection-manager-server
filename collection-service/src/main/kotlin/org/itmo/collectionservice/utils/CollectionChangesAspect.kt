package org.itmo.collectionservice.utils

import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.itmo.collectionservice.strategies.CollectionChangesStrategy
import org.springframework.stereotype.Component

@Aspect
@Component
class CollectionChangesAspect(private val collectionChangesStrategy: CollectionChangesStrategy) {
    @After("@annotation(org.itmo.collectionservice.annotations.ChangingCollection)")
    fun sendCollectionChanges() {
        collectionChangesStrategy.onCollectionUpdate()
    }
}