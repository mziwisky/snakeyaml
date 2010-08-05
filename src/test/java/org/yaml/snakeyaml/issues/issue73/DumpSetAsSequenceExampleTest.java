/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

package org.yaml.snakeyaml.issues.issue73;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.SnakeYaml;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

public class DumpSetAsSequenceExampleTest extends TestCase {

    public void testDumpFlow() {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        SnakeYaml yaml = new SnakeYaml(new Dumper(new SetRepresenter(), options));
        String output = yaml.dump(createBlog());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue73-dump7.txt"), output);
        //
        check(output);
    }

    public void testDumpBlock() {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        SnakeYaml yaml = new SnakeYaml(new Dumper(new SetRepresenter(), options));
        String output = yaml.dump(createBlog());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue73-dump8.txt"), output);
        //
        check(output);
    }

    private class SetRepresenter extends Representer {
        public SetRepresenter() {
            // when representer for Set is removed sets will be treated as
            // collections (!!seq)
            this.multiRepresenters.remove(Set.class);
        }
    }

    private Blog createBlog() {
        Blog blog = new Blog("Test Me!");
        blog.addPost(new Post("Title1", "text 1"));
        blog.addPost(new Post("Title2", "text text 2"));
        blog.numbers.add(19);
        blog.numbers.add(17);
        TreeSet<String> labels = new TreeSet<String>();
        labels.add("Java");
        labels.add("YAML");
        labels.add("SnakeYAML");
        blog.setLabels(labels);
        return blog;
    }

    private void check(String doc) {
        SnakeYaml yamlLoader = new SnakeYaml();
        yamlLoader.setBeanAccess(BeanAccess.FIELD);
        Blog blog = (Blog) yamlLoader.load(doc);
        assertEquals("Test Me!", blog.getName());
        assertEquals(2, blog.numbers.size());
        assertEquals(2, blog.getPosts().size());
        for (Post post : blog.getPosts()) {
            assertEquals(Post.class, post.getClass());
        }
    }
}