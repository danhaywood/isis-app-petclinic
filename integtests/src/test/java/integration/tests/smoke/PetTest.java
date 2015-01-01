/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package integration.tests.smoke;

import dom.pets.Pet;
import dom.pets.PetSpecies;
import dom.pets.Pets;
import fixture.pets.scenario.PetsFixture;
import integration.tests.PetClinicAppIntegTest;

import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PetTest extends PetClinicAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Pets pets;

    FixtureScript fixtureScript;
    Pet petPojo;
    Pet petWrapped;

    @Before
    public void setUp() throws Exception {

        // given
        fixtureScript = new PetsFixture();
        fixtureScripts.runFixtureScript(fixtureScript, null);

        petPojo = fixtureScript.lookup("pets-fixture/pet-for-fido/item-1", Pet.class);
        assertThat(petPojo, is(not(nullValue())));

        petWrapped = wrap(petPojo);
    }

    @Test
    public void doesNotExist() throws Exception {

        // when
        Pet petPojo = fixtureScript.lookup("non-existent", Pet.class);

        // then
        assertThat(petPojo, is(nullValue()));
    }

    public static class Name extends PetTest {

        @Test
        public void canChange() throws Exception {

            // given
            assertThat(petWrapped.getName(), is("Fido"));

            // when
            petWrapped.setName("Fred");

            // given
            assertThat(petWrapped.getName(), is("Fred"));
        }

    }

    public static class Species extends PetTest {

        @Test
        public void canChange() throws Exception {

            // given
            assertThat(petWrapped.getSpecies(), is(PetSpecies.Dog));

            // when
            petWrapped.setSpecies(PetSpecies.Cat);

            // given
            assertThat(petWrapped.getSpecies(), is(PetSpecies.Cat));
        }

    }

}