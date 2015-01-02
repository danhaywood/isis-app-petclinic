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

package fixture.owners.scenario;

import fixture.owners.objects.OwnerForBill;
import fixture.owners.objects.OwnerForFred;
import fixture.owners.objects.OwnerForJohn;
import fixture.owners.objects.OwnerForMary;
import fixture.pets.PetClinicAppTearDownFixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;

public class OwnersFixture extends FixtureScript {

    public OwnersFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Override
    protected void execute(ExecutionContext executionContext) {

        executionContext.executeChild(this, new PetClinicAppTearDownFixture());

        executionContext.executeChild(this, new OwnerForFred());
        executionContext.executeChild(this, new OwnerForBill());
        executionContext.executeChild(this, new OwnerForJohn());
        executionContext.executeChild(this, new OwnerForMary());
    }

}
