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
package integration.tests.owners;

import dom.owners.Owner;
import dom.pets.Pets;
import fixture.owners.scenario.OwnersFixture;
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

public class OwnerTest extends PetClinicAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Pets owners;

    FixtureScript fixtureScript;
    Owner ownerPojo;
    Owner ownerWrapped;

    @Before
    public void setUp() throws Exception {

        // given
        fixtureScript = new OwnersFixture();
        fixtureScripts.runFixtureScript(fixtureScript, null);

        ownerPojo = fixtureScript.lookup("owners-fixture/owner-for-bill/item-1", Owner.class);
        assertThat(ownerPojo, is(not(nullValue())));

        ownerWrapped = wrap(ownerPojo);
    }

    public static class Name extends OwnerTest {

        @Test
        public void canChange() throws Exception {

            // given
            assertThat(ownerWrapped.getName(), is("Bill"));

            // when
            ownerWrapped.setName("Bob");

            // given
            assertThat(ownerWrapped.getName(), is("Bob"));
        }

    }

}