/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package dom.owners;

import java.util.List;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OwnersTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    Owners owners;

    @Before
    public void setUp() throws Exception {
        owners = new Owners();
        owners.container = mockContainer;
    }

    public static class Create extends OwnersTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final Owner owner = new Owner();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(Owner.class);
                    inSequence(seq);
                    will(returnValue(owner));

                    oneOf(mockContainer).persistIfNotAlready(owner);
                    inSequence(seq);
                }
            });

            // when
            final Owner obj = owners.create("George");

            // then
            assertThat(obj, is(owner));
            assertThat(obj.getName(), is("George"));
        }
    }

    public static class ListAll extends OwnersTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Owner> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allInstances(Owner.class);
                    will(returnValue(all));
                }
            });

            // when
            final List<Owner> list = owners.listAll();

            // then
            assertThat(list, is(all));
        }
    }

    public static class FindByName extends OwnersTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Owner> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allMatches(with(query(Owner.class, "findByName", "name", ".*x.*")));
                    will(returnValue(all));
                }
            });

            // when
            final List<Owner> list = owners.findByName("x");

            // then
            assertThat(list, is(all));
        }

        private <T> Matcher<Query<T>> query(final Class<T> cls, final String queryName, final String param0, final Object arg0) {
            return new TypeSafeMatcher<Query<T>>() {
                @Override
                protected boolean matchesSafely(final Query<T> item) {
                    if (!(item instanceof QueryDefault)) {
                        return false;
                    }
                    final QueryDefault queryDefault = (QueryDefault) item;
                    return queryDefault.getResultType() == cls &&
                            queryDefault.getQueryName().equals(queryName) &&
                            queryDefault.getArgumentsByParameterName().equals(ImmutableMap.of(param0, arg0));
                }

                @Override
                public void describeTo(final Description description) {
                    description.appendText("query of " + cls.getName() + " using " + queryName);
                }
            };
        }

    }

}
