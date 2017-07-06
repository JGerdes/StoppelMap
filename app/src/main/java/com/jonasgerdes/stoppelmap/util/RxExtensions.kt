package com.jonasgerdes.stoppelmap.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 27-Jun-17
 */

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}
