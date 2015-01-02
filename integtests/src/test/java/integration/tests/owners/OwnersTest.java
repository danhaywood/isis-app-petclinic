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
import dom.owners.Owners;
import fixture.owners.scenario.OwnersFixture;
import fixture.pets.PetClinicAppTearDownFixture;
import integration.tests.PetClinicAppIntegTest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import static integration.tests.util.Util.causalChainContains;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OwnersTest extends PetClinicAppIntegTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Owners owners;

    FixtureScript fixtureScript;

    public static class ListAll extends OwnersTest {

        @Test
        public void happyCase() throws Exception {

            // given
            fixtureScript = new OwnersFixture();
            fixtureScripts.runFixtureScript(fixtureScript, null);
            nextTransaction();

            // when
            // (haven't wrapped 'owners' service because this is a prototype action
            // that isn't otherwise available in integ tests if wrapped)
            final List<Owner> all = owners.listAll();

            // then
            assertThat(all.size(), is(4));

            Owner owner = wrap(all.get(0));
            assertThat(owner.getName(), is("Fred"));
        }

        @Test
        public void whenNone() throws Exception {

            // given
            fixtureScript = new PetClinicAppTearDownFixture();
            fixtureScripts.runFixtureScript(fixtureScript, null);
            nextTransaction();

            // when
            // (haven't wrapped 'owners' service because this is a prototype action
            // that isn't otherwise available in integ tests if wrapped)
            final List<Owner> all = owners.listAll();

            // then
            assertThat(all.size(), is(0));
        }
    }

    public static class FindByName extends OwnersTest {

        @Test
        public void happyCase() throws Exception {

            // given
            fixtureScript = new OwnersFixture();
            fixtureScripts.runFixtureScript(fixtureScript, null);
            nextTransaction();

            // when
            final List<Owner> found = wrap(owners).findByName("r");

            // then
            assertThat(found.size(), is(2));

            Owner owner = wrap(found.get(0));
            assertThat(owner.getName(), is("Fred"));
            Owner owner2 = wrap(found.get(1));
            assertThat(owner2.getName(), is("Mary"));
        }

        @Test
        public void whenNone() throws Exception {

            // given
            fixtureScript = new OwnersFixture();
            fixtureScripts.runFixtureScript(fixtureScript, null);
            nextTransaction();

            // when
            final List<Owner> found = wrap(this.owners).findByName("zzz");

            // then
            assertThat(found.size(), is(0));
        }
    }

    public static class Create extends OwnersTest {

        @Test
        public void happyCase() throws Exception {

            // given
            fixtureScript = new PetClinicAppTearDownFixture();
            fixtureScripts.runFixtureScript(fixtureScript, null);
            nextTransaction();

            // when
            wrap(owners).create("Bill");

            // then
            final List<Owner> all = owners.listAll();
            assertThat(all.size(), is(1));
            final Owner owner = all.get(0);
            assertThat(owner.getName(), is("Bill"));
        }

        @Test
        public void whenAlreadyExists() throws Exception {

            // given
            fixtureScript = new PetClinicAppTearDownFixture();
            fixtureScripts.runFixtureScript(fixtureScript, null);
            nextTransaction();

            wrap(owners).create("Bill");
            nextTransaction();

            // then
            expectedException.expectCause(causalChainContains(SQLIntegrityConstraintViolationException.class));

            // when
            wrap(owners).create("Bill");
            nextTransaction();
        }

   }

}