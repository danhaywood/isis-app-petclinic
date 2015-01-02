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
package dom.pets;

import dom.owners.Owner;

import java.util.List;
import com.google.common.collect.Lists;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PetsTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    Pets pets;

    @Before
    public void setUp() throws Exception {
        pets = new Pets();
        pets.container = mockContainer;
    }

    public static class Create extends PetsTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final Pet pet = new Pet();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(Pet.class);
                    inSequence(seq);
                    will(returnValue(pet));

                    oneOf(mockContainer).persistIfNotAlready(pet);
                    inSequence(seq);
                }
            });

            // when
            final Owner owner = new Owner();
            final Pet obj = pets.create("Bonzo", PetSpecies.Dog, owner);

            // then
            assertThat(obj, is(pet));
            assertThat(obj.getName(), is("Bonzo"));
            assertThat(obj.getSpecies(), is(PetSpecies.Dog));
            assertThat(obj.getOwner(), is(owner));
        }

    }

    public static class ListAll extends PetsTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Pet> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allInstances(Pet.class);
                    will(returnValue(all));
                }
            });

            // when
            final List<Pet> list = pets.listAll();

            // then
            assertThat(list, is(all));
        }
    }
}
