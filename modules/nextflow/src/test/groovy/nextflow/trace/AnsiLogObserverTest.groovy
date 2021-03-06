/*
 * Copyright 2013-2019, Centre for Genomic Regulation (CRG)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nextflow.trace

import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class AnsiLogObserverTest extends Specification {

    @Unroll
    def 'should render a process line' () {

        given:
        def o = new AnsiLogObserver()
        def stats = new AnsiLogObserver.ProcessStats()
        stats.submitted = SUBMIT
        stats.completed = COMPLETED
        stats.cached    = CACHE
        stats.terminated = DONE
        stats.error = ERR

        expect:
        o.line('foo', stats) == EXPECTED

        where:
        SUBMIT  | COMPLETED | CACHE | DONE  | ERR   | EXPECTED
        1       | 0         | 0     | false | false | '> process foo [  0%] 0 of 1'
        1       | 1         | 0     | false | false | '> process foo [100%] 1 of 1'
        10      | 5         | 0     | false | false | '> process foo [ 50%] 5 of 10'
        0       | 0         | 5     | false | false | '> process foo [100%] 5 of 5, cached: 5'
        2       | 1         | 3     | false | false | '> process foo [ 80%] 4 of 5, cached: 3'
        2       | 2         | 0     | true  | false | '> process foo [100%] 2 of 2 ✓'
        2       | 2         | 0     | true  | true  | '> process foo [100%] 2 of 2 ✗'

    }

}
