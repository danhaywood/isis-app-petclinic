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
package dom.owners;

import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Prototype;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(repositoryFor = Owner.class)
@DomainServiceLayout(menuOrder = "20")
public class Owners {

    //region > listAll (action)

    @Bookmarkable
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    @Prototype
    public List<Owner> listAll() {
        return container.allInstances(Owner.class);
    }

    //endregion

    //region > create (action)
    @MemberOrder(sequence = "2")
    public Owner create(
            final @ParameterLayout(named = "Name") String name) {
        final Owner obj = container.newTransientInstance(Owner.class);
        obj.setName(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > findByName (action)
    @MemberOrder(sequence = "1")
    public List<Owner> findByName(
            @ParameterLayout(named = "Name")
            final String name) {
        final String nameArg = String.format(".*%s.*", name);
        final List<Owner> owners = container.allMatches(
                new QueryDefault<>(
                        Owner.class,
                        "findByName",
                        "name", nameArg));
        return owners;
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    DomainObjectContainer container;
    //endregion

}
