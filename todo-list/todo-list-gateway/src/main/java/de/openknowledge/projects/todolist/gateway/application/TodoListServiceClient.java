/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.projects.todolist.gateway.application;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/todos")
public interface TodoListServiceClient {

  @POST
  Response createTodo(final String newTodo);

  @DELETE
  @Path("/{todoId}")
  Response deleteTodo(@PathParam(value = "todoId") final Long todoId);

  @GET
  @Path("/{todoId}")
  Response getTodo(@PathParam(value = "todoId") final Long todoId);

  @GET
  Response getTodos();

  @PUT
  @Path("/{todoId}")
  Response updateTodo(@PathParam(value = "todoId") final Long todoId, String modifiedTodo);
}
