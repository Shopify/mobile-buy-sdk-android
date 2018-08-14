// Generated from graphql_java_gen gem

package com.shopify.buy3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Arguments;
import com.shopify.graphql.support.Error;
import com.shopify.graphql.support.Query;
import com.shopify.graphql.support.SchemaViolationError;
import com.shopify.graphql.support.TopLevelResponse;
import com.shopify.graphql.support.Input;

import com.shopify.graphql.support.ID;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Storefront {
    public static QueryRootQuery query(QueryRootQueryDefinition queryDef) {
        StringBuilder queryString = new StringBuilder("{");
        QueryRootQuery query = new QueryRootQuery(queryString);
        queryDef.define(query);
        queryString.append('}');
        return query;
    }

    public static class QueryResponse {
        private TopLevelResponse response;
        private QueryRoot data;

        public QueryResponse(TopLevelResponse response) throws SchemaViolationError {
            this.response = response;
            this.data = response.getData() != null ? new QueryRoot(response.getData()) : null;
        }

        public QueryRoot getData() {
            return data;
        }

        public List<Error> getErrors() {
            return response.getErrors();
        }

        public String toJson() {
            return new Gson().toJson(response);
        }

        public String prettyPrintJson() {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(response);
        }

        public static QueryResponse fromJson(String json) throws SchemaViolationError {
            final TopLevelResponse response = new Gson().fromJson(json, TopLevelResponse.class);
            return new QueryResponse(response);
        }
    }

    public static MutationQuery mutation(MutationQueryDefinition queryDef) {
        StringBuilder queryString = new StringBuilder("mutation{");
        MutationQuery query = new MutationQuery(queryString);
        queryDef.define(query);
        queryString.append('}');
        return query;
    }

    public static class MutationResponse {
        private TopLevelResponse response;
        private Mutation data;

        public MutationResponse(TopLevelResponse response) throws SchemaViolationError {
            this.response = response;
            this.data = response.getData() != null ? new Mutation(response.getData()) : null;
        }

        public Mutation getData() {
            return data;
        }

        public List<Error> getErrors() {
            return response.getErrors();
        }

        public String toJson() {
            return new Gson().toJson(response);
        }

        public String prettyPrintJson() {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(response);
        }

        public static MutationResponse fromJson(String json) throws SchemaViolationError {
            final TopLevelResponse response = new Gson().fromJson(json, TopLevelResponse.class);
            return new MutationResponse(response);
        }
    }

    public interface AppliedGiftCardQueryDefinition {
        void define(AppliedGiftCardQuery _queryBuilder);
    }

    /**
    * Details about the gift card used on the checkout.
    */
    public static class AppliedGiftCardQuery extends Query<AppliedGiftCardQuery> {
        AppliedGiftCardQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The amount that was used taken from the Gift Card by applying it.
        */
        public AppliedGiftCardQuery amountUsed() {
            startField("amountUsed");

            return this;
        }

        /**
        * The amount left on the Gift Card.
        */
        public AppliedGiftCardQuery balance() {
            startField("balance");

            return this;
        }

        /**
        * The last characters of the Gift Card code
        */
        public AppliedGiftCardQuery lastCharacters() {
            startField("lastCharacters");

            return this;
        }
    }

    /**
    * Details about the gift card used on the checkout.
    */
    public static class AppliedGiftCard extends AbstractResponse<AppliedGiftCard> implements Node {
        public AppliedGiftCard() {
        }

        public AppliedGiftCard(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "amountUsed": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "balance": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lastCharacters": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public AppliedGiftCard(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "AppliedGiftCard";
        }

        /**
        * The amount that was used taken from the Gift Card by applying it.
        */

        public BigDecimal getAmountUsed() {
            return (BigDecimal) get("amountUsed");
        }

        public AppliedGiftCard setAmountUsed(BigDecimal arg) {
            optimisticData.put(getKey("amountUsed"), arg);
            return this;
        }

        /**
        * The amount left on the Gift Card.
        */

        public BigDecimal getBalance() {
            return (BigDecimal) get("balance");
        }

        public AppliedGiftCard setBalance(BigDecimal arg) {
            optimisticData.put(getKey("balance"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The last characters of the Gift Card code
        */

        public String getLastCharacters() {
            return (String) get("lastCharacters");
        }

        public AppliedGiftCard setLastCharacters(String arg) {
            optimisticData.put(getKey("lastCharacters"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "amountUsed": return false;

                case "balance": return false;

                case "id": return false;

                case "lastCharacters": return false;

                default: return false;
            }
        }
    }

    public interface ArticleQueryDefinition {
        void define(ArticleQuery _queryBuilder);
    }

    public static class ArticleQuery extends Query<ArticleQuery> {
        ArticleQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The article's author.
        */
        public ArticleQuery author(ArticleAuthorQueryDefinition queryDef) {
            startField("author");

            _queryBuilder.append('{');
            queryDef.define(new ArticleAuthorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The blog that the article belongs to.
        */
        public ArticleQuery blog(BlogQueryDefinition queryDef) {
            startField("blog");

            _queryBuilder.append('{');
            queryDef.define(new BlogQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class CommentsArguments extends Arguments {
            CommentsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public CommentsArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public CommentsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public CommentsArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public CommentsArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public CommentsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface CommentsArgumentsDefinition {
            void define(CommentsArguments args);
        }

        /**
        * List of comments posted on the article.
        */
        public ArticleQuery comments(CommentConnectionQueryDefinition queryDef) {
            return comments(args -> {}, queryDef);
        }

        /**
        * List of comments posted on the article.
        */
        public ArticleQuery comments(CommentsArgumentsDefinition argsDef, CommentConnectionQueryDefinition queryDef) {
            startField("comments");

            CommentsArguments args = new CommentsArguments(_queryBuilder);
            argsDef.define(args);
            CommentsArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new CommentConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class ContentArguments extends Arguments {
            ContentArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncates string after the given length.
            */
            public ContentArguments truncateAt(Integer value) {
                if (value != null) {
                    startArgument("truncateAt");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ContentArgumentsDefinition {
            void define(ContentArguments args);
        }

        /**
        * Stripped content of the article, single line with HTML tags removed.
        */
        public ArticleQuery content() {
            return content(args -> {});
        }

        /**
        * Stripped content of the article, single line with HTML tags removed.
        */
        public ArticleQuery content(ContentArgumentsDefinition argsDef) {
            startField("content");

            ContentArguments args = new ContentArguments(_queryBuilder);
            argsDef.define(args);
            ContentArguments.end(args);

            return this;
        }

        /**
        * The content of the article, complete with HTML formatting.
        */
        public ArticleQuery contentHtml() {
            startField("contentHtml");

            return this;
        }

        public class ExcerptArguments extends Arguments {
            ExcerptArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncates string after the given length.
            */
            public ExcerptArguments truncateAt(Integer value) {
                if (value != null) {
                    startArgument("truncateAt");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ExcerptArgumentsDefinition {
            void define(ExcerptArguments args);
        }

        /**
        * Stripped excerpt of the article, single line with HTML tags removed.
        */
        public ArticleQuery excerpt() {
            return excerpt(args -> {});
        }

        /**
        * Stripped excerpt of the article, single line with HTML tags removed.
        */
        public ArticleQuery excerpt(ExcerptArgumentsDefinition argsDef) {
            startField("excerpt");

            ExcerptArguments args = new ExcerptArguments(_queryBuilder);
            argsDef.define(args);
            ExcerptArguments.end(args);

            return this;
        }

        /**
        * The excerpt of the article, complete with HTML formatting.
        */
        public ArticleQuery excerptHtml() {
            startField("excerptHtml");

            return this;
        }

        /**
        * A human-friendly unique string for the Article automatically generated from its title.
        */
        public ArticleQuery handle() {
            startField("handle");

            return this;
        }

        public class ImageArguments extends Arguments {
            ImageArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Image width in pixels between 1 and 2048. This argument is deprecated: Use `maxWidth` on
            * `Image.transformedSrc` instead.
            */
            public ImageArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image height in pixels between 1 and 2048. This argument is deprecated: Use `maxHeight` on
            * `Image.transformedSrc` instead.
            */
            public ImageArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Crops the image according to the specified region. This argument is deprecated: Use `crop` on
            * `Image.transformedSrc` instead.
            */
            public ImageArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Image size multiplier for high-resolution retina displays. Must be between 1 and 3. This argument is
            * deprecated: Use `scale` on `Image.transformedSrc` instead.
            */
            public ImageArguments scale(Integer value) {
                if (value != null) {
                    startArgument("scale");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ImageArgumentsDefinition {
            void define(ImageArguments args);
        }

        /**
        * The image associated with the article.
        */
        public ArticleQuery image(ImageQueryDefinition queryDef) {
            return image(args -> {}, queryDef);
        }

        /**
        * The image associated with the article.
        */
        public ArticleQuery image(ImageArgumentsDefinition argsDef, ImageQueryDefinition queryDef) {
            startField("image");

            ImageArguments args = new ImageArguments(_queryBuilder);
            argsDef.define(args);
            ImageArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ImageQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The date and time when the article was published.
        */
        public ArticleQuery publishedAt() {
            startField("publishedAt");

            return this;
        }

        /**
        * A categorization that a article can be tagged with.
        */
        public ArticleQuery tags() {
            startField("tags");

            return this;
        }

        /**
        * The article’s name.
        */
        public ArticleQuery title() {
            startField("title");

            return this;
        }

        /**
        * The url pointing to the article accessible from the web.
        */
        public ArticleQuery url() {
            startField("url");

            return this;
        }
    }

    public static class Article extends AbstractResponse<Article> implements Node {
        public Article() {
        }

        public Article(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "author": {
                        responseData.put(key, new ArticleAuthor(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "blog": {
                        responseData.put(key, new Blog(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "comments": {
                        responseData.put(key, new CommentConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "content": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "contentHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "excerpt": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "excerptHtml": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "handle": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "image": {
                        Image optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Image(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "publishedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "tags": {
                        List<String> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(jsonAsString(element1, key));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "url": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Article(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Article";
        }

        /**
        * The article's author.
        */

        public ArticleAuthor getAuthor() {
            return (ArticleAuthor) get("author");
        }

        public Article setAuthor(ArticleAuthor arg) {
            optimisticData.put(getKey("author"), arg);
            return this;
        }

        /**
        * The blog that the article belongs to.
        */

        public Blog getBlog() {
            return (Blog) get("blog");
        }

        public Article setBlog(Blog arg) {
            optimisticData.put(getKey("blog"), arg);
            return this;
        }

        /**
        * List of comments posted on the article.
        */

        public CommentConnection getComments() {
            return (CommentConnection) get("comments");
        }

        public Article setComments(CommentConnection arg) {
            optimisticData.put(getKey("comments"), arg);
            return this;
        }

        /**
        * Stripped content of the article, single line with HTML tags removed.
        */

        public String getContent() {
            return (String) get("content");
        }

        public Article setContent(String arg) {
            optimisticData.put(getKey("content"), arg);
            return this;
        }

        /**
        * The content of the article, complete with HTML formatting.
        */

        public String getContentHtml() {
            return (String) get("contentHtml");
        }

        public Article setContentHtml(String arg) {
            optimisticData.put(getKey("contentHtml"), arg);
            return this;
        }

        /**
        * Stripped excerpt of the article, single line with HTML tags removed.
        */

        public String getExcerpt() {
            return (String) get("excerpt");
        }

        public Article setExcerpt(String arg) {
            optimisticData.put(getKey("excerpt"), arg);
            return this;
        }

        /**
        * The excerpt of the article, complete with HTML formatting.
        */

        public String getExcerptHtml() {
            return (String) get("excerptHtml");
        }

        public Article setExcerptHtml(String arg) {
            optimisticData.put(getKey("excerptHtml"), arg);
            return this;
        }

        /**
        * A human-friendly unique string for the Article automatically generated from its title.
        */

        public String getHandle() {
            return (String) get("handle");
        }

        public Article setHandle(String arg) {
            optimisticData.put(getKey("handle"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The image associated with the article.
        */

        public Image getImage() {
            return (Image) get("image");
        }

        public Article setImage(Image arg) {
            optimisticData.put(getKey("image"), arg);
            return this;
        }

        /**
        * The date and time when the article was published.
        */

        public DateTime getPublishedAt() {
            return (DateTime) get("publishedAt");
        }

        public Article setPublishedAt(DateTime arg) {
            optimisticData.put(getKey("publishedAt"), arg);
            return this;
        }

        /**
        * A categorization that a article can be tagged with.
        */

        public List<String> getTags() {
            return (List<String>) get("tags");
        }

        public Article setTags(List<String> arg) {
            optimisticData.put(getKey("tags"), arg);
            return this;
        }

        /**
        * The article’s name.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public Article setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The url pointing to the article accessible from the web.
        */

        public String getUrl() {
            return (String) get("url");
        }

        public Article setUrl(String arg) {
            optimisticData.put(getKey("url"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "author": return true;

                case "blog": return true;

                case "comments": return true;

                case "content": return false;

                case "contentHtml": return false;

                case "excerpt": return false;

                case "excerptHtml": return false;

                case "handle": return false;

                case "id": return false;

                case "image": return true;

                case "publishedAt": return false;

                case "tags": return false;

                case "title": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public interface ArticleAuthorQueryDefinition {
        void define(ArticleAuthorQuery _queryBuilder);
    }

    public static class ArticleAuthorQuery extends Query<ArticleAuthorQuery> {
        ArticleAuthorQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The author's bio.
        */
        public ArticleAuthorQuery bio() {
            startField("bio");

            return this;
        }

        /**
        * The author’s email.
        */
        public ArticleAuthorQuery email() {
            startField("email");

            return this;
        }

        /**
        * The author's first name.
        */
        public ArticleAuthorQuery firstName() {
            startField("firstName");

            return this;
        }

        /**
        * The author's last name.
        */
        public ArticleAuthorQuery lastName() {
            startField("lastName");

            return this;
        }

        /**
        * The author's full name
        */
        public ArticleAuthorQuery name() {
            startField("name");

            return this;
        }
    }

    public static class ArticleAuthor extends AbstractResponse<ArticleAuthor> {
        public ArticleAuthor() {
        }

        public ArticleAuthor(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "bio": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "email": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "firstName": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "lastName": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "name": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ArticleAuthor";
        }

        /**
        * The author's bio.
        */

        public String getBio() {
            return (String) get("bio");
        }

        public ArticleAuthor setBio(String arg) {
            optimisticData.put(getKey("bio"), arg);
            return this;
        }

        /**
        * The author’s email.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public ArticleAuthor setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        /**
        * The author's first name.
        */

        public String getFirstName() {
            return (String) get("firstName");
        }

        public ArticleAuthor setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        /**
        * The author's last name.
        */

        public String getLastName() {
            return (String) get("lastName");
        }

        public ArticleAuthor setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        /**
        * The author's full name
        */

        public String getName() {
            return (String) get("name");
        }

        public ArticleAuthor setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "bio": return false;

                case "email": return false;

                case "firstName": return false;

                case "lastName": return false;

                case "name": return false;

                default: return false;
            }
        }
    }

    public interface ArticleConnectionQueryDefinition {
        void define(ArticleConnectionQuery _queryBuilder);
    }

    public static class ArticleConnectionQuery extends Query<ArticleConnectionQuery> {
        ArticleConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public ArticleConnectionQuery edges(ArticleEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ArticleEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public ArticleConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ArticleConnection extends AbstractResponse<ArticleConnection> {
        public ArticleConnection() {
        }

        public ArticleConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<ArticleEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new ArticleEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ArticleConnection";
        }

        /**
        * A list of edges.
        */

        public List<ArticleEdge> getEdges() {
            return (List<ArticleEdge>) get("edges");
        }

        public ArticleConnection setEdges(List<ArticleEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ArticleConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface ArticleEdgeQueryDefinition {
        void define(ArticleEdgeQuery _queryBuilder);
    }

    public static class ArticleEdgeQuery extends Query<ArticleEdgeQuery> {
        ArticleEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public ArticleEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of ArticleEdge.
        */
        public ArticleEdgeQuery node(ArticleQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new ArticleQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ArticleEdge extends AbstractResponse<ArticleEdge> {
        public ArticleEdge() {
        }

        public ArticleEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Article(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ArticleEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public ArticleEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of ArticleEdge.
        */

        public Article getNode() {
            return (Article) get("node");
        }

        public ArticleEdge setNode(Article arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    /**
    * The set of valid sort keys for the articles query.
    */
    public enum ArticleSortKeys {
        AUTHOR,

        BLOG_TITLE,

        ID,

        /**
        * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
        * the
        * results by relevance to the search term(s). When no search query is specified, this sort key is not
        * deterministic and should not be used.
        */
        RELEVANCE,

        TITLE,

        UPDATED_AT,

        UNKNOWN_VALUE;

        public static ArticleSortKeys fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AUTHOR": {
                    return AUTHOR;
                }

                case "BLOG_TITLE": {
                    return BLOG_TITLE;
                }

                case "ID": {
                    return ID;
                }

                case "RELEVANCE": {
                    return RELEVANCE;
                }

                case "TITLE": {
                    return TITLE;
                }

                case "UPDATED_AT": {
                    return UPDATED_AT;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AUTHOR: {
                    return "AUTHOR";
                }

                case BLOG_TITLE: {
                    return "BLOG_TITLE";
                }

                case ID: {
                    return "ID";
                }

                case RELEVANCE: {
                    return "RELEVANCE";
                }

                case TITLE: {
                    return "TITLE";
                }

                case UPDATED_AT: {
                    return "UPDATED_AT";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface AttributeQueryDefinition {
        void define(AttributeQuery _queryBuilder);
    }

    /**
    * Represents a generic custom attribute.
    */
    public static class AttributeQuery extends Query<AttributeQuery> {
        AttributeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Key or name of the attribute.
        */
        public AttributeQuery key() {
            startField("key");

            return this;
        }

        /**
        * Value of the attribute.
        */
        public AttributeQuery value() {
            startField("value");

            return this;
        }
    }

    /**
    * Represents a generic custom attribute.
    */
    public static class Attribute extends AbstractResponse<Attribute> {
        public Attribute() {
        }

        public Attribute(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "key": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "value": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Attribute";
        }

        /**
        * Key or name of the attribute.
        */

        public String getKey() {
            return (String) get("key");
        }

        public Attribute setKey(String arg) {
            optimisticData.put(getKey("key"), arg);
            return this;
        }

        /**
        * Value of the attribute.
        */

        public String getValue() {
            return (String) get("value");
        }

        public Attribute setValue(String arg) {
            optimisticData.put(getKey("value"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "key": return false;

                case "value": return false;

                default: return false;
            }
        }
    }

    public static class AttributeInput implements Serializable {
        private String key;

        private String value;

        public AttributeInput(String key, String value) {
            this.key = key;

            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public AttributeInput setKey(String key) {
            this.key = key;
            return this;
        }

        public String getValue() {
            return value;
        }

        public AttributeInput setValue(String value) {
            this.value = value;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("key:");
            Query.appendQuotedString(_queryBuilder, key.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("value:");
            Query.appendQuotedString(_queryBuilder, value.toString());

            _queryBuilder.append('}');
        }
    }

    public interface AvailableShippingRatesQueryDefinition {
        void define(AvailableShippingRatesQuery _queryBuilder);
    }

    /**
    * A collection of available shipping rates for a checkout.
    */
    public static class AvailableShippingRatesQuery extends Query<AvailableShippingRatesQuery> {
        AvailableShippingRatesQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Whether or not the shipping rates are ready.
        * The `shippingRates` field is `null` when this value is `false`.
        * This field should be polled until its value becomes `true`.
        */
        public AvailableShippingRatesQuery ready() {
            startField("ready");

            return this;
        }

        /**
        * The fetched shipping rates. `null` until the `ready` field is `true`.
        */
        public AvailableShippingRatesQuery shippingRates(ShippingRateQueryDefinition queryDef) {
            startField("shippingRates");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * A collection of available shipping rates for a checkout.
    */
    public static class AvailableShippingRates extends AbstractResponse<AvailableShippingRates> {
        public AvailableShippingRates() {
        }

        public AvailableShippingRates(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "ready": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "shippingRates": {
                        List<ShippingRate> optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            List<ShippingRate> list1 = new ArrayList<>();
                            for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                list1.add(new ShippingRate(jsonAsObject(element1, key)));
                            }

                            optional1 = list1;
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "AvailableShippingRates";
        }

        /**
        * Whether or not the shipping rates are ready.
        * The `shippingRates` field is `null` when this value is `false`.
        * This field should be polled until its value becomes `true`.
        */

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public AvailableShippingRates setReady(Boolean arg) {
            optimisticData.put(getKey("ready"), arg);
            return this;
        }

        /**
        * The fetched shipping rates. `null` until the `ready` field is `true`.
        */

        public List<ShippingRate> getShippingRates() {
            return (List<ShippingRate>) get("shippingRates");
        }

        public AvailableShippingRates setShippingRates(List<ShippingRate> arg) {
            optimisticData.put(getKey("shippingRates"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "ready": return false;

                case "shippingRates": return true;

                default: return false;
            }
        }
    }

    public interface BlogQueryDefinition {
        void define(BlogQuery _queryBuilder);
    }

    public static class BlogQuery extends Query<BlogQuery> {
        BlogQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public class ArticlesArguments extends Arguments {
            ArticlesArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public ArticlesArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public ArticlesArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public ArticlesArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public ArticlesArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public ArticlesArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ArticlesArgumentsDefinition {
            void define(ArticlesArguments args);
        }

        /**
        * List of the blog's articles.
        */
        public BlogQuery articles(ArticleConnectionQueryDefinition queryDef) {
            return articles(args -> {}, queryDef);
        }

        /**
        * List of the blog's articles.
        */
        public BlogQuery articles(ArticlesArgumentsDefinition argsDef, ArticleConnectionQueryDefinition queryDef) {
            startField("articles");

            ArticlesArguments args = new ArticlesArguments(_queryBuilder);
            argsDef.define(args);
            ArticlesArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ArticleConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A human-friendly unique string for the Blog automatically generated from its title.
        */
        public BlogQuery handle() {
            startField("handle");

            return this;
        }

        /**
        * The blogs’s title.
        */
        public BlogQuery title() {
            startField("title");

            return this;
        }

        /**
        * The url pointing to the blog accessible from the web.
        */
        public BlogQuery url() {
            startField("url");

            return this;
        }
    }

    public static class Blog extends AbstractResponse<Blog> implements Node {
        public Blog() {
        }

        public Blog(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "articles": {
                        responseData.put(key, new ArticleConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "handle": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "url": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Blog(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Blog";
        }

        /**
        * List of the blog's articles.
        */

        public ArticleConnection getArticles() {
            return (ArticleConnection) get("articles");
        }

        public Blog setArticles(ArticleConnection arg) {
            optimisticData.put(getKey("articles"), arg);
            return this;
        }

        /**
        * A human-friendly unique string for the Blog automatically generated from its title.
        */

        public String getHandle() {
            return (String) get("handle");
        }

        public Blog setHandle(String arg) {
            optimisticData.put(getKey("handle"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The blogs’s title.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public Blog setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The url pointing to the blog accessible from the web.
        */

        public String getUrl() {
            return (String) get("url");
        }

        public Blog setUrl(String arg) {
            optimisticData.put(getKey("url"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "articles": return true;

                case "handle": return false;

                case "id": return false;

                case "title": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public interface BlogConnectionQueryDefinition {
        void define(BlogConnectionQuery _queryBuilder);
    }

    public static class BlogConnectionQuery extends Query<BlogConnectionQuery> {
        BlogConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public BlogConnectionQuery edges(BlogEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new BlogEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public BlogConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class BlogConnection extends AbstractResponse<BlogConnection> {
        public BlogConnection() {
        }

        public BlogConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<BlogEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new BlogEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "BlogConnection";
        }

        /**
        * A list of edges.
        */

        public List<BlogEdge> getEdges() {
            return (List<BlogEdge>) get("edges");
        }

        public BlogConnection setEdges(List<BlogEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public BlogConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface BlogEdgeQueryDefinition {
        void define(BlogEdgeQuery _queryBuilder);
    }

    public static class BlogEdgeQuery extends Query<BlogEdgeQuery> {
        BlogEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public BlogEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of BlogEdge.
        */
        public BlogEdgeQuery node(BlogQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new BlogQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class BlogEdge extends AbstractResponse<BlogEdge> {
        public BlogEdge() {
        }

        public BlogEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Blog(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "BlogEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public BlogEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of BlogEdge.
        */

        public Blog getNode() {
            return (Blog) get("node");
        }

        public BlogEdge setNode(Blog arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    /**
    * The set of valid sort keys for the blogs query.
    */
    public enum BlogSortKeys {
        HANDLE,

        ID,

        /**
        * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
        * the
        * results by relevance to the search term(s). When no search query is specified, this sort key is not
        * deterministic and should not be used.
        */
        RELEVANCE,

        TITLE,

        UNKNOWN_VALUE;

        public static BlogSortKeys fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "HANDLE": {
                    return HANDLE;
                }

                case "ID": {
                    return ID;
                }

                case "RELEVANCE": {
                    return RELEVANCE;
                }

                case "TITLE": {
                    return TITLE;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case HANDLE: {
                    return "HANDLE";
                }

                case ID: {
                    return "ID";
                }

                case RELEVANCE: {
                    return "RELEVANCE";
                }

                case TITLE: {
                    return "TITLE";
                }

                default: {
                    return "";
                }
            }
        }
    }

    /**
    * Card brand, such as Visa or Mastercard, which can be used for payments.
    */
    public enum CardBrand {
        /**
        * American Express
        */
        AMERICAN_EXPRESS,

        /**
        * Diners Club
        */
        DINERS_CLUB,

        /**
        * Discover
        */
        DISCOVER,

        /**
        * JCB
        */
        JCB,

        /**
        * Mastercard
        */
        MASTERCARD,

        /**
        * Visa
        */
        VISA,

        UNKNOWN_VALUE;

        public static CardBrand fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AMERICAN_EXPRESS": {
                    return AMERICAN_EXPRESS;
                }

                case "DINERS_CLUB": {
                    return DINERS_CLUB;
                }

                case "DISCOVER": {
                    return DISCOVER;
                }

                case "JCB": {
                    return JCB;
                }

                case "MASTERCARD": {
                    return MASTERCARD;
                }

                case "VISA": {
                    return VISA;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AMERICAN_EXPRESS: {
                    return "AMERICAN_EXPRESS";
                }

                case DINERS_CLUB: {
                    return "DINERS_CLUB";
                }

                case DISCOVER: {
                    return "DISCOVER";
                }

                case JCB: {
                    return "JCB";
                }

                case MASTERCARD: {
                    return "MASTERCARD";
                }

                case VISA: {
                    return "VISA";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CheckoutQueryDefinition {
        void define(CheckoutQuery _queryBuilder);
    }

    /**
    * A container for all the information required to checkout items and pay.
    */
    public static class CheckoutQuery extends Query<CheckoutQuery> {
        CheckoutQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public CheckoutQuery appliedGiftCards(AppliedGiftCardQueryDefinition queryDef) {
            startField("appliedGiftCards");

            _queryBuilder.append('{');
            queryDef.define(new AppliedGiftCardQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The available shipping rates for this Checkout.
        * Should only be used when checkout `requiresShipping` is `true` and
        * the shipping address is valid.
        */
        public CheckoutQuery availableShippingRates(AvailableShippingRatesQueryDefinition queryDef) {
            startField("availableShippingRates");

            _queryBuilder.append('{');
            queryDef.define(new AvailableShippingRatesQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The date and time when the checkout was completed.
        */
        public CheckoutQuery completedAt() {
            startField("completedAt");

            return this;
        }

        /**
        * The date and time when the checkout was created.
        */
        public CheckoutQuery createdAt() {
            startField("createdAt");

            return this;
        }

        /**
        * The currency code for the Checkout.
        */
        public CheckoutQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        /**
        * A list of extra information that is added to the checkout.
        */
        public CheckoutQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The customer associated with the checkout.
        *
        * @deprecated This field will always return null. If you have an authentication token for the customer, you can use the `customer` field on the query root to retrieve it.
        */
        @Deprecated
        public CheckoutQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The email attached to this checkout.
        */
        public CheckoutQuery email() {
            startField("email");

            return this;
        }

        public class LineItemsArguments extends Arguments {
            LineItemsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public LineItemsArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public LineItemsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public LineItemsArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public LineItemsArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public LineItemsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface LineItemsArgumentsDefinition {
            void define(LineItemsArguments args);
        }

        /**
        * A list of line item objects, each one containing information about an item in the checkout.
        */
        public CheckoutQuery lineItems(CheckoutLineItemConnectionQueryDefinition queryDef) {
            return lineItems(args -> {}, queryDef);
        }

        /**
        * A list of line item objects, each one containing information about an item in the checkout.
        */
        public CheckoutQuery lineItems(LineItemsArgumentsDefinition argsDef, CheckoutLineItemConnectionQueryDefinition queryDef) {
            startField("lineItems");

            LineItemsArguments args = new LineItemsArguments(_queryBuilder);
            argsDef.define(args);
            LineItemsArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutQuery note() {
            startField("note");

            return this;
        }

        /**
        * The resulting order from a paid checkout.
        */
        public CheckoutQuery order(OrderQueryDefinition queryDef) {
            startField("order");

            _queryBuilder.append('{');
            queryDef.define(new OrderQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The Order Status Page for this Checkout, null when checkout is not completed.
        */
        public CheckoutQuery orderStatusUrl() {
            startField("orderStatusUrl");

            return this;
        }

        /**
        * The amount left to be paid. This is equal to the cost of the line items, taxes and shipping minus
        * discounts and gift cards.
        */
        public CheckoutQuery paymentDue() {
            startField("paymentDue");

            return this;
        }

        /**
        * Whether or not the Checkout is ready and can be completed. Checkouts may have asynchronous
        * operations that can take time to finish. If you want to complete a checkout or ensure all the fields
        * are populated and up to date, polling is required until the value is true. 
        */
        public CheckoutQuery ready() {
            startField("ready");

            return this;
        }

        /**
        * States whether or not the fulfillment requires shipping.
        */
        public CheckoutQuery requiresShipping() {
            startField("requiresShipping");

            return this;
        }

        /**
        * The shipping address to where the line items will be shipped.
        */
        public CheckoutQuery shippingAddress(MailingAddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Once a shipping rate is selected by the customer it is transitioned to a `shipping_line` object.
        */
        public CheckoutQuery shippingLine(ShippingRateQueryDefinition queryDef) {
            startField("shippingLine");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Price of the checkout before shipping, taxes, and discounts.
        */
        public CheckoutQuery subtotalPrice() {
            startField("subtotalPrice");

            return this;
        }

        /**
        * Specifies if the Checkout is tax exempt.
        */
        public CheckoutQuery taxExempt() {
            startField("taxExempt");

            return this;
        }

        /**
        * Specifies if taxes are included in the line item and shipping line prices.
        */
        public CheckoutQuery taxesIncluded() {
            startField("taxesIncluded");

            return this;
        }

        /**
        * The sum of all the prices of all the items in the checkout, taxes and discounts included.
        */
        public CheckoutQuery totalPrice() {
            startField("totalPrice");

            return this;
        }

        /**
        * The sum of all the taxes applied to the line items and shipping lines in the checkout.
        */
        public CheckoutQuery totalTax() {
            startField("totalTax");

            return this;
        }

        /**
        * The date and time when the checkout was last updated.
        */
        public CheckoutQuery updatedAt() {
            startField("updatedAt");

            return this;
        }

        /**
        * The url pointing to the checkout accessible from the web.
        */
        public CheckoutQuery webUrl() {
            startField("webUrl");

            return this;
        }
    }

    /**
    * A container for all the information required to checkout items and pay.
    */
    public static class Checkout extends AbstractResponse<Checkout> implements Node {
        public Checkout() {
        }

        public Checkout(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "appliedGiftCards": {
                        List<AppliedGiftCard> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new AppliedGiftCard(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "availableShippingRates": {
                        AvailableShippingRates optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new AvailableShippingRates(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "completedAt": {
                        DateTime optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = Utils.parseDateTime(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "currencyCode": {
                        responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "customAttributes": {
                        List<Attribute> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Attribute(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "email": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lineItems": {
                        responseData.put(key, new CheckoutLineItemConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "note": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "order": {
                        Order optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Order(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "orderStatusUrl": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "paymentDue": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "ready": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "requiresShipping": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "shippingAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "shippingLine": {
                        ShippingRate optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShippingRate(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "subtotalPrice": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "taxExempt": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "taxesIncluded": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "totalPrice": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "totalTax": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "webUrl": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Checkout(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Checkout";
        }

        public List<AppliedGiftCard> getAppliedGiftCards() {
            return (List<AppliedGiftCard>) get("appliedGiftCards");
        }

        public Checkout setAppliedGiftCards(List<AppliedGiftCard> arg) {
            optimisticData.put(getKey("appliedGiftCards"), arg);
            return this;
        }

        /**
        * The available shipping rates for this Checkout.
        * Should only be used when checkout `requiresShipping` is `true` and
        * the shipping address is valid.
        */

        public AvailableShippingRates getAvailableShippingRates() {
            return (AvailableShippingRates) get("availableShippingRates");
        }

        public Checkout setAvailableShippingRates(AvailableShippingRates arg) {
            optimisticData.put(getKey("availableShippingRates"), arg);
            return this;
        }

        /**
        * The date and time when the checkout was completed.
        */

        public DateTime getCompletedAt() {
            return (DateTime) get("completedAt");
        }

        public Checkout setCompletedAt(DateTime arg) {
            optimisticData.put(getKey("completedAt"), arg);
            return this;
        }

        /**
        * The date and time when the checkout was created.
        */

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Checkout setCreatedAt(DateTime arg) {
            optimisticData.put(getKey("createdAt"), arg);
            return this;
        }

        /**
        * The currency code for the Checkout.
        */

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Checkout setCurrencyCode(CurrencyCode arg) {
            optimisticData.put(getKey("currencyCode"), arg);
            return this;
        }

        /**
        * A list of extra information that is added to the checkout.
        */

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public Checkout setCustomAttributes(List<Attribute> arg) {
            optimisticData.put(getKey("customAttributes"), arg);
            return this;
        }

        /**
        * The customer associated with the checkout.
        *
        * @deprecated This field will always return null. If you have an authentication token for the customer, you can use the `customer` field on the query root to retrieve it.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public Checkout setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * The email attached to this checkout.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public Checkout setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * A list of line item objects, each one containing information about an item in the checkout.
        */

        public CheckoutLineItemConnection getLineItems() {
            return (CheckoutLineItemConnection) get("lineItems");
        }

        public Checkout setLineItems(CheckoutLineItemConnection arg) {
            optimisticData.put(getKey("lineItems"), arg);
            return this;
        }

        public String getNote() {
            return (String) get("note");
        }

        public Checkout setNote(String arg) {
            optimisticData.put(getKey("note"), arg);
            return this;
        }

        /**
        * The resulting order from a paid checkout.
        */

        public Order getOrder() {
            return (Order) get("order");
        }

        public Checkout setOrder(Order arg) {
            optimisticData.put(getKey("order"), arg);
            return this;
        }

        /**
        * The Order Status Page for this Checkout, null when checkout is not completed.
        */

        public String getOrderStatusUrl() {
            return (String) get("orderStatusUrl");
        }

        public Checkout setOrderStatusUrl(String arg) {
            optimisticData.put(getKey("orderStatusUrl"), arg);
            return this;
        }

        /**
        * The amount left to be paid. This is equal to the cost of the line items, taxes and shipping minus
        * discounts and gift cards.
        */

        public BigDecimal getPaymentDue() {
            return (BigDecimal) get("paymentDue");
        }

        public Checkout setPaymentDue(BigDecimal arg) {
            optimisticData.put(getKey("paymentDue"), arg);
            return this;
        }

        /**
        * Whether or not the Checkout is ready and can be completed. Checkouts may have asynchronous
        * operations that can take time to finish. If you want to complete a checkout or ensure all the fields
        * are populated and up to date, polling is required until the value is true. 
        */

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public Checkout setReady(Boolean arg) {
            optimisticData.put(getKey("ready"), arg);
            return this;
        }

        /**
        * States whether or not the fulfillment requires shipping.
        */

        public Boolean getRequiresShipping() {
            return (Boolean) get("requiresShipping");
        }

        public Checkout setRequiresShipping(Boolean arg) {
            optimisticData.put(getKey("requiresShipping"), arg);
            return this;
        }

        /**
        * The shipping address to where the line items will be shipped.
        */

        public MailingAddress getShippingAddress() {
            return (MailingAddress) get("shippingAddress");
        }

        public Checkout setShippingAddress(MailingAddress arg) {
            optimisticData.put(getKey("shippingAddress"), arg);
            return this;
        }

        /**
        * Once a shipping rate is selected by the customer it is transitioned to a `shipping_line` object.
        */

        public ShippingRate getShippingLine() {
            return (ShippingRate) get("shippingLine");
        }

        public Checkout setShippingLine(ShippingRate arg) {
            optimisticData.put(getKey("shippingLine"), arg);
            return this;
        }

        /**
        * Price of the checkout before shipping, taxes, and discounts.
        */

        public BigDecimal getSubtotalPrice() {
            return (BigDecimal) get("subtotalPrice");
        }

        public Checkout setSubtotalPrice(BigDecimal arg) {
            optimisticData.put(getKey("subtotalPrice"), arg);
            return this;
        }

        /**
        * Specifies if the Checkout is tax exempt.
        */

        public Boolean getTaxExempt() {
            return (Boolean) get("taxExempt");
        }

        public Checkout setTaxExempt(Boolean arg) {
            optimisticData.put(getKey("taxExempt"), arg);
            return this;
        }

        /**
        * Specifies if taxes are included in the line item and shipping line prices.
        */

        public Boolean getTaxesIncluded() {
            return (Boolean) get("taxesIncluded");
        }

        public Checkout setTaxesIncluded(Boolean arg) {
            optimisticData.put(getKey("taxesIncluded"), arg);
            return this;
        }

        /**
        * The sum of all the prices of all the items in the checkout, taxes and discounts included.
        */

        public BigDecimal getTotalPrice() {
            return (BigDecimal) get("totalPrice");
        }

        public Checkout setTotalPrice(BigDecimal arg) {
            optimisticData.put(getKey("totalPrice"), arg);
            return this;
        }

        /**
        * The sum of all the taxes applied to the line items and shipping lines in the checkout.
        */

        public BigDecimal getTotalTax() {
            return (BigDecimal) get("totalTax");
        }

        public Checkout setTotalTax(BigDecimal arg) {
            optimisticData.put(getKey("totalTax"), arg);
            return this;
        }

        /**
        * The date and time when the checkout was last updated.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Checkout setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        /**
        * The url pointing to the checkout accessible from the web.
        */

        public String getWebUrl() {
            return (String) get("webUrl");
        }

        public Checkout setWebUrl(String arg) {
            optimisticData.put(getKey("webUrl"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "appliedGiftCards": return true;

                case "availableShippingRates": return true;

                case "completedAt": return false;

                case "createdAt": return false;

                case "currencyCode": return false;

                case "customAttributes": return true;

                case "customer": return true;

                case "email": return false;

                case "id": return false;

                case "lineItems": return true;

                case "note": return false;

                case "order": return true;

                case "orderStatusUrl": return false;

                case "paymentDue": return false;

                case "ready": return false;

                case "requiresShipping": return false;

                case "shippingAddress": return true;

                case "shippingLine": return true;

                case "subtotalPrice": return false;

                case "taxExempt": return false;

                case "taxesIncluded": return false;

                case "totalPrice": return false;

                case "totalTax": return false;

                case "updatedAt": return false;

                case "webUrl": return false;

                default: return false;
            }
        }
    }

    public static class CheckoutAttributesUpdateInput implements Serializable {
        private Input<String> note = Input.undefined();

        private Input<List<AttributeInput>> customAttributes = Input.undefined();

        private Input<Boolean> allowPartialAddresses = Input.undefined();

        public String getNote() {
            return note.getValue();
        }

        public Input<String> getNoteInput() {
            return note;
        }

        public CheckoutAttributesUpdateInput setNote(String note) {
            this.note = Input.optional(note);
            return this;
        }

        public CheckoutAttributesUpdateInput setNoteInput(Input<String> note) {
            if (note == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.note = note;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes.getValue();
        }

        public Input<List<AttributeInput>> getCustomAttributesInput() {
            return customAttributes;
        }

        public CheckoutAttributesUpdateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = Input.optional(customAttributes);
            return this;
        }

        public CheckoutAttributesUpdateInput setCustomAttributesInput(Input<List<AttributeInput>> customAttributes) {
            if (customAttributes == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.customAttributes = customAttributes;
            return this;
        }

        public Boolean getAllowPartialAddresses() {
            return allowPartialAddresses.getValue();
        }

        public Input<Boolean> getAllowPartialAddressesInput() {
            return allowPartialAddresses;
        }

        public CheckoutAttributesUpdateInput setAllowPartialAddresses(Boolean allowPartialAddresses) {
            this.allowPartialAddresses = Input.optional(allowPartialAddresses);
            return this;
        }

        public CheckoutAttributesUpdateInput setAllowPartialAddressesInput(Input<Boolean> allowPartialAddresses) {
            if (allowPartialAddresses == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.allowPartialAddresses = allowPartialAddresses;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (this.note.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("note:");
                if (note.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, note.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.customAttributes.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                if (customAttributes.getValue() != null) {
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (AttributeInput item1 : customAttributes.getValue()) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.allowPartialAddresses.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("allowPartialAddresses:");
                if (allowPartialAddresses.getValue() != null) {
                    _queryBuilder.append(allowPartialAddresses.getValue());
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutAttributesUpdatePayloadQueryDefinition {
        void define(CheckoutAttributesUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutAttributesUpdatePayloadQuery extends Query<CheckoutAttributesUpdatePayloadQuery> {
        CheckoutAttributesUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutAttributesUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutAttributesUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutAttributesUpdatePayload extends AbstractResponse<CheckoutAttributesUpdatePayload> {
        public CheckoutAttributesUpdatePayload() {
        }

        public CheckoutAttributesUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutAttributesUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutAttributesUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutAttributesUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCompleteFreePayloadQueryDefinition {
        void define(CheckoutCompleteFreePayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteFreePayloadQuery extends Query<CheckoutCompleteFreePayloadQuery> {
        CheckoutCompleteFreePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutCompleteFreePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCompleteFreePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCompleteFreePayload extends AbstractResponse<CheckoutCompleteFreePayload> {
        public CheckoutCompleteFreePayload() {
        }

        public CheckoutCompleteFreePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteFreePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteFreePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteFreePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCompleteWithCreditCardPayloadQueryDefinition {
        void define(CheckoutCompleteWithCreditCardPayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteWithCreditCardPayloadQuery extends Query<CheckoutCompleteWithCreditCardPayloadQuery> {
        CheckoutCompleteWithCreditCardPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The checkout on which the payment was applied.
        */
        public CheckoutCompleteWithCreditCardPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A representation of the attempted payment.
        */
        public CheckoutCompleteWithCreditCardPayloadQuery payment(PaymentQueryDefinition queryDef) {
            startField("payment");

            _queryBuilder.append('{');
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCompleteWithCreditCardPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCompleteWithCreditCardPayload extends AbstractResponse<CheckoutCompleteWithCreditCardPayload> {
        public CheckoutCompleteWithCreditCardPayload() {
        }

        public CheckoutCompleteWithCreditCardPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "payment": {
                        Payment optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Payment(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteWithCreditCardPayload";
        }

        /**
        * The checkout on which the payment was applied.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteWithCreditCardPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * A representation of the attempted payment.
        */

        public Payment getPayment() {
            return (Payment) get("payment");
        }

        public CheckoutCompleteWithCreditCardPayload setPayment(Payment arg) {
            optimisticData.put(getKey("payment"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteWithCreditCardPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "payment": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition {
        void define(CheckoutCompleteWithTokenizedPaymentPayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteWithTokenizedPaymentPayloadQuery extends Query<CheckoutCompleteWithTokenizedPaymentPayloadQuery> {
        CheckoutCompleteWithTokenizedPaymentPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The checkout on which the payment was applied.
        */
        public CheckoutCompleteWithTokenizedPaymentPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A representation of the attempted payment.
        */
        public CheckoutCompleteWithTokenizedPaymentPayloadQuery payment(PaymentQueryDefinition queryDef) {
            startField("payment");

            _queryBuilder.append('{');
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCompleteWithTokenizedPaymentPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCompleteWithTokenizedPaymentPayload extends AbstractResponse<CheckoutCompleteWithTokenizedPaymentPayload> {
        public CheckoutCompleteWithTokenizedPaymentPayload() {
        }

        public CheckoutCompleteWithTokenizedPaymentPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "payment": {
                        Payment optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Payment(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteWithTokenizedPaymentPayload";
        }

        /**
        * The checkout on which the payment was applied.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * A representation of the attempted payment.
        */

        public Payment getPayment() {
            return (Payment) get("payment");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setPayment(Payment arg) {
            optimisticData.put(getKey("payment"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "payment": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutCreateInput implements Serializable {
        private Input<String> email = Input.undefined();

        private Input<List<CheckoutLineItemInput>> lineItems = Input.undefined();

        private Input<MailingAddressInput> shippingAddress = Input.undefined();

        private Input<String> note = Input.undefined();

        private Input<List<AttributeInput>> customAttributes = Input.undefined();

        private Input<Boolean> allowPartialAddresses = Input.undefined();

        public String getEmail() {
            return email.getValue();
        }

        public Input<String> getEmailInput() {
            return email;
        }

        public CheckoutCreateInput setEmail(String email) {
            this.email = Input.optional(email);
            return this;
        }

        public CheckoutCreateInput setEmailInput(Input<String> email) {
            if (email == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.email = email;
            return this;
        }

        public List<CheckoutLineItemInput> getLineItems() {
            return lineItems.getValue();
        }

        public Input<List<CheckoutLineItemInput>> getLineItemsInput() {
            return lineItems;
        }

        public CheckoutCreateInput setLineItems(List<CheckoutLineItemInput> lineItems) {
            this.lineItems = Input.optional(lineItems);
            return this;
        }

        public CheckoutCreateInput setLineItemsInput(Input<List<CheckoutLineItemInput>> lineItems) {
            if (lineItems == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.lineItems = lineItems;
            return this;
        }

        public MailingAddressInput getShippingAddress() {
            return shippingAddress.getValue();
        }

        public Input<MailingAddressInput> getShippingAddressInput() {
            return shippingAddress;
        }

        public CheckoutCreateInput setShippingAddress(MailingAddressInput shippingAddress) {
            this.shippingAddress = Input.optional(shippingAddress);
            return this;
        }

        public CheckoutCreateInput setShippingAddressInput(Input<MailingAddressInput> shippingAddress) {
            if (shippingAddress == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.shippingAddress = shippingAddress;
            return this;
        }

        public String getNote() {
            return note.getValue();
        }

        public Input<String> getNoteInput() {
            return note;
        }

        public CheckoutCreateInput setNote(String note) {
            this.note = Input.optional(note);
            return this;
        }

        public CheckoutCreateInput setNoteInput(Input<String> note) {
            if (note == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.note = note;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes.getValue();
        }

        public Input<List<AttributeInput>> getCustomAttributesInput() {
            return customAttributes;
        }

        public CheckoutCreateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = Input.optional(customAttributes);
            return this;
        }

        public CheckoutCreateInput setCustomAttributesInput(Input<List<AttributeInput>> customAttributes) {
            if (customAttributes == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.customAttributes = customAttributes;
            return this;
        }

        public Boolean getAllowPartialAddresses() {
            return allowPartialAddresses.getValue();
        }

        public Input<Boolean> getAllowPartialAddressesInput() {
            return allowPartialAddresses;
        }

        public CheckoutCreateInput setAllowPartialAddresses(Boolean allowPartialAddresses) {
            this.allowPartialAddresses = Input.optional(allowPartialAddresses);
            return this;
        }

        public CheckoutCreateInput setAllowPartialAddressesInput(Input<Boolean> allowPartialAddresses) {
            if (allowPartialAddresses == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.allowPartialAddresses = allowPartialAddresses;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (this.email.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                if (email.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, email.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.lineItems.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lineItems:");
                if (lineItems.getValue() != null) {
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (CheckoutLineItemInput item1 : lineItems.getValue()) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.shippingAddress.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("shippingAddress:");
                if (shippingAddress.getValue() != null) {
                    shippingAddress.getValue().appendTo(_queryBuilder);
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.note.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("note:");
                if (note.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, note.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.customAttributes.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                if (customAttributes.getValue() != null) {
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (AttributeInput item1 : customAttributes.getValue()) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.allowPartialAddresses.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("allowPartialAddresses:");
                if (allowPartialAddresses.getValue() != null) {
                    _queryBuilder.append(allowPartialAddresses.getValue());
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutCreatePayloadQueryDefinition {
        void define(CheckoutCreatePayloadQuery _queryBuilder);
    }

    public static class CheckoutCreatePayloadQuery extends Query<CheckoutCreatePayloadQuery> {
        CheckoutCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The new checkout object.
        */
        public CheckoutCreatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCreatePayload extends AbstractResponse<CheckoutCreatePayload> {
        public CheckoutCreatePayload() {
        }

        public CheckoutCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCreatePayload";
        }

        /**
        * The new checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCreatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCustomerAssociatePayloadQueryDefinition {
        void define(CheckoutCustomerAssociatePayloadQuery _queryBuilder);
    }

    public static class CheckoutCustomerAssociatePayloadQuery extends Query<CheckoutCustomerAssociatePayloadQuery> {
        CheckoutCustomerAssociatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutCustomerAssociatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The associated customer object.
        */
        public CheckoutCustomerAssociatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCustomerAssociatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCustomerAssociatePayload extends AbstractResponse<CheckoutCustomerAssociatePayload> {
        public CheckoutCustomerAssociatePayload() {
        }

        public CheckoutCustomerAssociatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCustomerAssociatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCustomerAssociatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * The associated customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CheckoutCustomerAssociatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCustomerAssociatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCustomerDisassociatePayloadQueryDefinition {
        void define(CheckoutCustomerDisassociatePayloadQuery _queryBuilder);
    }

    public static class CheckoutCustomerDisassociatePayloadQuery extends Query<CheckoutCustomerDisassociatePayloadQuery> {
        CheckoutCustomerDisassociatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutCustomerDisassociatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCustomerDisassociatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCustomerDisassociatePayload extends AbstractResponse<CheckoutCustomerDisassociatePayload> {
        public CheckoutCustomerDisassociatePayload() {
        }

        public CheckoutCustomerDisassociatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCustomerDisassociatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCustomerDisassociatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCustomerDisassociatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutDiscountCodeApplyPayloadQueryDefinition {
        void define(CheckoutDiscountCodeApplyPayloadQuery _queryBuilder);
    }

    public static class CheckoutDiscountCodeApplyPayloadQuery extends Query<CheckoutDiscountCodeApplyPayloadQuery> {
        CheckoutDiscountCodeApplyPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutDiscountCodeApplyPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutDiscountCodeApplyPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutDiscountCodeApplyPayload extends AbstractResponse<CheckoutDiscountCodeApplyPayload> {
        public CheckoutDiscountCodeApplyPayload() {
        }

        public CheckoutDiscountCodeApplyPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutDiscountCodeApplyPayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutDiscountCodeApplyPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutDiscountCodeApplyPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutDiscountCodeRemovePayloadQueryDefinition {
        void define(CheckoutDiscountCodeRemovePayloadQuery _queryBuilder);
    }

    public static class CheckoutDiscountCodeRemovePayloadQuery extends Query<CheckoutDiscountCodeRemovePayloadQuery> {
        CheckoutDiscountCodeRemovePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutDiscountCodeRemovePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutDiscountCodeRemovePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutDiscountCodeRemovePayload extends AbstractResponse<CheckoutDiscountCodeRemovePayload> {
        public CheckoutDiscountCodeRemovePayload() {
        }

        public CheckoutDiscountCodeRemovePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutDiscountCodeRemovePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutDiscountCodeRemovePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutDiscountCodeRemovePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutEmailUpdatePayloadQueryDefinition {
        void define(CheckoutEmailUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutEmailUpdatePayloadQuery extends Query<CheckoutEmailUpdatePayloadQuery> {
        CheckoutEmailUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The checkout object with the updated email.
        */
        public CheckoutEmailUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutEmailUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutEmailUpdatePayload extends AbstractResponse<CheckoutEmailUpdatePayload> {
        public CheckoutEmailUpdatePayload() {
        }

        public CheckoutEmailUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutEmailUpdatePayload";
        }

        /**
        * The checkout object with the updated email.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutEmailUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutEmailUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutGiftCardApplyPayloadQueryDefinition {
        void define(CheckoutGiftCardApplyPayloadQuery _queryBuilder);
    }

    public static class CheckoutGiftCardApplyPayloadQuery extends Query<CheckoutGiftCardApplyPayloadQuery> {
        CheckoutGiftCardApplyPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutGiftCardApplyPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutGiftCardApplyPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutGiftCardApplyPayload extends AbstractResponse<CheckoutGiftCardApplyPayload> {
        public CheckoutGiftCardApplyPayload() {
        }

        public CheckoutGiftCardApplyPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutGiftCardApplyPayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutGiftCardApplyPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutGiftCardApplyPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutGiftCardRemovePayloadQueryDefinition {
        void define(CheckoutGiftCardRemovePayloadQuery _queryBuilder);
    }

    public static class CheckoutGiftCardRemovePayloadQuery extends Query<CheckoutGiftCardRemovePayloadQuery> {
        CheckoutGiftCardRemovePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutGiftCardRemovePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutGiftCardRemovePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutGiftCardRemovePayload extends AbstractResponse<CheckoutGiftCardRemovePayload> {
        public CheckoutGiftCardRemovePayload() {
        }

        public CheckoutGiftCardRemovePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutGiftCardRemovePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutGiftCardRemovePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutGiftCardRemovePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutGiftCardsAppendPayloadQueryDefinition {
        void define(CheckoutGiftCardsAppendPayloadQuery _queryBuilder);
    }

    public static class CheckoutGiftCardsAppendPayloadQuery extends Query<CheckoutGiftCardsAppendPayloadQuery> {
        CheckoutGiftCardsAppendPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutGiftCardsAppendPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutGiftCardsAppendPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutGiftCardsAppendPayload extends AbstractResponse<CheckoutGiftCardsAppendPayload> {
        public CheckoutGiftCardsAppendPayload() {
        }

        public CheckoutGiftCardsAppendPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutGiftCardsAppendPayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutGiftCardsAppendPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutGiftCardsAppendPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemQueryDefinition {
        void define(CheckoutLineItemQuery _queryBuilder);
    }

    /**
    * A single line item in the checkout, grouped by variant and attributes.
    */
    public static class CheckoutLineItemQuery extends Query<CheckoutLineItemQuery> {
        CheckoutLineItemQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * Extra information in the form of an array of Key-Value pairs about the line item.
        */
        public CheckoutLineItemQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The quantity of the line item.
        */
        public CheckoutLineItemQuery quantity() {
            startField("quantity");

            return this;
        }

        /**
        * Title of the line item. Defaults to the product's title.
        */
        public CheckoutLineItemQuery title() {
            startField("title");

            return this;
        }

        /**
        * Product variant of the line item.
        */
        public CheckoutLineItemQuery variant(ProductVariantQueryDefinition queryDef) {
            startField("variant");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * A single line item in the checkout, grouped by variant and attributes.
    */
    public static class CheckoutLineItem extends AbstractResponse<CheckoutLineItem> implements Node {
        public CheckoutLineItem() {
        }

        public CheckoutLineItem(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customAttributes": {
                        List<Attribute> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Attribute(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "quantity": {
                        responseData.put(key, jsonAsInteger(field.getValue(), key));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "variant": {
                        ProductVariant optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ProductVariant(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public CheckoutLineItem(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItem";
        }

        /**
        * Extra information in the form of an array of Key-Value pairs about the line item.
        */

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public CheckoutLineItem setCustomAttributes(List<Attribute> arg) {
            optimisticData.put(getKey("customAttributes"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The quantity of the line item.
        */

        public Integer getQuantity() {
            return (Integer) get("quantity");
        }

        public CheckoutLineItem setQuantity(Integer arg) {
            optimisticData.put(getKey("quantity"), arg);
            return this;
        }

        /**
        * Title of the line item. Defaults to the product's title.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public CheckoutLineItem setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * Product variant of the line item.
        */

        public ProductVariant getVariant() {
            return (ProductVariant) get("variant");
        }

        public CheckoutLineItem setVariant(ProductVariant arg) {
            optimisticData.put(getKey("variant"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customAttributes": return true;

                case "id": return false;

                case "quantity": return false;

                case "title": return false;

                case "variant": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemConnectionQueryDefinition {
        void define(CheckoutLineItemConnectionQuery _queryBuilder);
    }

    public static class CheckoutLineItemConnectionQuery extends Query<CheckoutLineItemConnectionQuery> {
        CheckoutLineItemConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public CheckoutLineItemConnectionQuery edges(CheckoutLineItemEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public CheckoutLineItemConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemConnection extends AbstractResponse<CheckoutLineItemConnection> {
        public CheckoutLineItemConnection() {
        }

        public CheckoutLineItemConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<CheckoutLineItemEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new CheckoutLineItemEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemConnection";
        }

        /**
        * A list of edges.
        */

        public List<CheckoutLineItemEdge> getEdges() {
            return (List<CheckoutLineItemEdge>) get("edges");
        }

        public CheckoutLineItemConnection setEdges(List<CheckoutLineItemEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public CheckoutLineItemConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemEdgeQueryDefinition {
        void define(CheckoutLineItemEdgeQuery _queryBuilder);
    }

    public static class CheckoutLineItemEdgeQuery extends Query<CheckoutLineItemEdgeQuery> {
        CheckoutLineItemEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public CheckoutLineItemEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of CheckoutLineItemEdge.
        */
        public CheckoutLineItemEdgeQuery node(CheckoutLineItemQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemEdge extends AbstractResponse<CheckoutLineItemEdge> {
        public CheckoutLineItemEdge() {
        }

        public CheckoutLineItemEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new CheckoutLineItem(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public CheckoutLineItemEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of CheckoutLineItemEdge.
        */

        public CheckoutLineItem getNode() {
            return (CheckoutLineItem) get("node");
        }

        public CheckoutLineItemEdge setNode(CheckoutLineItem arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutLineItemInput implements Serializable {
        private int quantity;

        private ID variantId;

        private Input<List<AttributeInput>> customAttributes = Input.undefined();

        public CheckoutLineItemInput(int quantity, ID variantId) {
            this.quantity = quantity;

            this.variantId = variantId;
        }

        public int getQuantity() {
            return quantity;
        }

        public CheckoutLineItemInput setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ID getVariantId() {
            return variantId;
        }

        public CheckoutLineItemInput setVariantId(ID variantId) {
            this.variantId = variantId;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes.getValue();
        }

        public Input<List<AttributeInput>> getCustomAttributesInput() {
            return customAttributes;
        }

        public CheckoutLineItemInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = Input.optional(customAttributes);
            return this;
        }

        public CheckoutLineItemInput setCustomAttributesInput(Input<List<AttributeInput>> customAttributes) {
            if (customAttributes == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.customAttributes = customAttributes;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("quantity:");
            _queryBuilder.append(quantity);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("variantId:");
            Query.appendQuotedString(_queryBuilder, variantId.toString());

            if (this.customAttributes.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                if (customAttributes.getValue() != null) {
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (AttributeInput item1 : customAttributes.getValue()) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public static class CheckoutLineItemUpdateInput implements Serializable {
        private Input<ID> id = Input.undefined();

        private Input<ID> variantId = Input.undefined();

        private Input<Integer> quantity = Input.undefined();

        private Input<List<AttributeInput>> customAttributes = Input.undefined();

        public ID getId() {
            return id.getValue();
        }

        public Input<ID> getIdInput() {
            return id;
        }

        public CheckoutLineItemUpdateInput setId(ID id) {
            this.id = Input.optional(id);
            return this;
        }

        public CheckoutLineItemUpdateInput setIdInput(Input<ID> id) {
            if (id == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.id = id;
            return this;
        }

        public ID getVariantId() {
            return variantId.getValue();
        }

        public Input<ID> getVariantIdInput() {
            return variantId;
        }

        public CheckoutLineItemUpdateInput setVariantId(ID variantId) {
            this.variantId = Input.optional(variantId);
            return this;
        }

        public CheckoutLineItemUpdateInput setVariantIdInput(Input<ID> variantId) {
            if (variantId == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.variantId = variantId;
            return this;
        }

        public Integer getQuantity() {
            return quantity.getValue();
        }

        public Input<Integer> getQuantityInput() {
            return quantity;
        }

        public CheckoutLineItemUpdateInput setQuantity(Integer quantity) {
            this.quantity = Input.optional(quantity);
            return this;
        }

        public CheckoutLineItemUpdateInput setQuantityInput(Input<Integer> quantity) {
            if (quantity == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.quantity = quantity;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes.getValue();
        }

        public Input<List<AttributeInput>> getCustomAttributesInput() {
            return customAttributes;
        }

        public CheckoutLineItemUpdateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = Input.optional(customAttributes);
            return this;
        }

        public CheckoutLineItemUpdateInput setCustomAttributesInput(Input<List<AttributeInput>> customAttributes) {
            if (customAttributes == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.customAttributes = customAttributes;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (this.id.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("id:");
                if (id.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, id.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.variantId.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("variantId:");
                if (variantId.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, variantId.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.quantity.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("quantity:");
                if (quantity.getValue() != null) {
                    _queryBuilder.append(quantity.getValue());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.customAttributes.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                if (customAttributes.getValue() != null) {
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (AttributeInput item1 : customAttributes.getValue()) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutLineItemsAddPayloadQueryDefinition {
        void define(CheckoutLineItemsAddPayloadQuery _queryBuilder);
    }

    public static class CheckoutLineItemsAddPayloadQuery extends Query<CheckoutLineItemsAddPayloadQuery> {
        CheckoutLineItemsAddPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutLineItemsAddPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutLineItemsAddPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemsAddPayload extends AbstractResponse<CheckoutLineItemsAddPayload> {
        public CheckoutLineItemsAddPayload() {
        }

        public CheckoutLineItemsAddPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemsAddPayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutLineItemsAddPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutLineItemsAddPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemsRemovePayloadQueryDefinition {
        void define(CheckoutLineItemsRemovePayloadQuery _queryBuilder);
    }

    public static class CheckoutLineItemsRemovePayloadQuery extends Query<CheckoutLineItemsRemovePayloadQuery> {
        CheckoutLineItemsRemovePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutLineItemsRemovePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutLineItemsRemovePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemsRemovePayload extends AbstractResponse<CheckoutLineItemsRemovePayload> {
        public CheckoutLineItemsRemovePayload() {
        }

        public CheckoutLineItemsRemovePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemsRemovePayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutLineItemsRemovePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutLineItemsRemovePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemsUpdatePayloadQueryDefinition {
        void define(CheckoutLineItemsUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutLineItemsUpdatePayloadQuery extends Query<CheckoutLineItemsUpdatePayloadQuery> {
        CheckoutLineItemsUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutLineItemsUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutLineItemsUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemsUpdatePayload extends AbstractResponse<CheckoutLineItemsUpdatePayload> {
        public CheckoutLineItemsUpdatePayload() {
        }

        public CheckoutLineItemsUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemsUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutLineItemsUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutLineItemsUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutShippingAddressUpdatePayloadQueryDefinition {
        void define(CheckoutShippingAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutShippingAddressUpdatePayloadQuery extends Query<CheckoutShippingAddressUpdatePayloadQuery> {
        CheckoutShippingAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutShippingAddressUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutShippingAddressUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutShippingAddressUpdatePayload extends AbstractResponse<CheckoutShippingAddressUpdatePayload> {
        public CheckoutShippingAddressUpdatePayload() {
        }

        public CheckoutShippingAddressUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutShippingAddressUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutShippingAddressUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutShippingAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutShippingLineUpdatePayloadQueryDefinition {
        void define(CheckoutShippingLineUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutShippingLineUpdatePayloadQuery extends Query<CheckoutShippingLineUpdatePayloadQuery> {
        CheckoutShippingLineUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutShippingLineUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutShippingLineUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutShippingLineUpdatePayload extends AbstractResponse<CheckoutShippingLineUpdatePayload> {
        public CheckoutShippingLineUpdatePayload() {
        }

        public CheckoutShippingLineUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutShippingLineUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutShippingLineUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutShippingLineUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CollectionQueryDefinition {
        void define(CollectionQuery _queryBuilder);
    }

    /**
    * A collection represents a grouping of products that a shop owner can create to organize them or make
    * their shops easier to browse.
    */
    public static class CollectionQuery extends Query<CollectionQuery> {
        CollectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public class DescriptionArguments extends Arguments {
            DescriptionArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncates string after the given length.
            */
            public DescriptionArguments truncateAt(Integer value) {
                if (value != null) {
                    startArgument("truncateAt");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface DescriptionArgumentsDefinition {
            void define(DescriptionArguments args);
        }

        /**
        * Stripped description of the collection, single line with HTML tags removed.
        */
        public CollectionQuery description() {
            return description(args -> {});
        }

        /**
        * Stripped description of the collection, single line with HTML tags removed.
        */
        public CollectionQuery description(DescriptionArgumentsDefinition argsDef) {
            startField("description");

            DescriptionArguments args = new DescriptionArguments(_queryBuilder);
            argsDef.define(args);
            DescriptionArguments.end(args);

            return this;
        }

        /**
        * The description of the collection, complete with HTML formatting.
        */
        public CollectionQuery descriptionHtml() {
            startField("descriptionHtml");

            return this;
        }

        /**
        * A human-friendly unique string for the collection automatically generated from its title.
        * Limit of 255 characters.
        */
        public CollectionQuery handle() {
            startField("handle");

            return this;
        }

        public class ImageArguments extends Arguments {
            ImageArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Image width in pixels between 1 and 2048. This argument is deprecated: Use `maxWidth` on
            * `Image.transformedSrc` instead.
            */
            public ImageArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image height in pixels between 1 and 2048. This argument is deprecated: Use `maxHeight` on
            * `Image.transformedSrc` instead.
            */
            public ImageArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Crops the image according to the specified region. This argument is deprecated: Use `crop` on
            * `Image.transformedSrc` instead.
            */
            public ImageArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Image size multiplier for high-resolution retina displays. Must be between 1 and 3. This argument is
            * deprecated: Use `scale` on `Image.transformedSrc` instead.
            */
            public ImageArguments scale(Integer value) {
                if (value != null) {
                    startArgument("scale");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ImageArgumentsDefinition {
            void define(ImageArguments args);
        }

        /**
        * Image associated with the collection.
        */
        public CollectionQuery image(ImageQueryDefinition queryDef) {
            return image(args -> {}, queryDef);
        }

        /**
        * Image associated with the collection.
        */
        public CollectionQuery image(ImageArgumentsDefinition argsDef, ImageQueryDefinition queryDef) {
            startField("image");

            ImageArguments args = new ImageArguments(_queryBuilder);
            argsDef.define(args);
            ImageArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ImageQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class ProductsArguments extends Arguments {
            ProductsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public ProductsArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public ProductsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public ProductsArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public ProductsArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public ProductsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Sort the underlying list by the given key.
            */
            public ProductsArguments sortKey(ProductCollectionSortKeys value) {
                if (value != null) {
                    startArgument("sortKey");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }
        }

        public interface ProductsArgumentsDefinition {
            void define(ProductsArguments args);
        }

        /**
        * List of products in the collection.
        */
        public CollectionQuery products(ProductConnectionQueryDefinition queryDef) {
            return products(args -> {}, queryDef);
        }

        /**
        * List of products in the collection.
        */
        public CollectionQuery products(ProductsArgumentsDefinition argsDef, ProductConnectionQueryDefinition queryDef) {
            startField("products");

            ProductsArguments args = new ProductsArguments(_queryBuilder);
            argsDef.define(args);
            ProductsArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ProductConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The collection’s name. Limit of 255 characters.
        */
        public CollectionQuery title() {
            startField("title");

            return this;
        }

        /**
        * The date and time when the collection was last modified.
        */
        public CollectionQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    /**
    * A collection represents a grouping of products that a shop owner can create to organize them or make
    * their shops easier to browse.
    */
    public static class Collection extends AbstractResponse<Collection> implements Node {
        public Collection() {
        }

        public Collection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "description": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "descriptionHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "handle": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "image": {
                        Image optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Image(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "products": {
                        responseData.put(key, new ProductConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Collection(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Collection";
        }

        /**
        * Stripped description of the collection, single line with HTML tags removed.
        */

        public String getDescription() {
            return (String) get("description");
        }

        public Collection setDescription(String arg) {
            optimisticData.put(getKey("description"), arg);
            return this;
        }

        /**
        * The description of the collection, complete with HTML formatting.
        */

        public String getDescriptionHtml() {
            return (String) get("descriptionHtml");
        }

        public Collection setDescriptionHtml(String arg) {
            optimisticData.put(getKey("descriptionHtml"), arg);
            return this;
        }

        /**
        * A human-friendly unique string for the collection automatically generated from its title.
        * Limit of 255 characters.
        */

        public String getHandle() {
            return (String) get("handle");
        }

        public Collection setHandle(String arg) {
            optimisticData.put(getKey("handle"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * Image associated with the collection.
        */

        public Image getImage() {
            return (Image) get("image");
        }

        public Collection setImage(Image arg) {
            optimisticData.put(getKey("image"), arg);
            return this;
        }

        /**
        * List of products in the collection.
        */

        public ProductConnection getProducts() {
            return (ProductConnection) get("products");
        }

        public Collection setProducts(ProductConnection arg) {
            optimisticData.put(getKey("products"), arg);
            return this;
        }

        /**
        * The collection’s name. Limit of 255 characters.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public Collection setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The date and time when the collection was last modified.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Collection setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "description": return false;

                case "descriptionHtml": return false;

                case "handle": return false;

                case "id": return false;

                case "image": return true;

                case "products": return true;

                case "title": return false;

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public interface CollectionConnectionQueryDefinition {
        void define(CollectionConnectionQuery _queryBuilder);
    }

    public static class CollectionConnectionQuery extends Query<CollectionConnectionQuery> {
        CollectionConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public CollectionConnectionQuery edges(CollectionEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new CollectionEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public CollectionConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CollectionConnection extends AbstractResponse<CollectionConnection> {
        public CollectionConnection() {
        }

        public CollectionConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<CollectionEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new CollectionEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CollectionConnection";
        }

        /**
        * A list of edges.
        */

        public List<CollectionEdge> getEdges() {
            return (List<CollectionEdge>) get("edges");
        }

        public CollectionConnection setEdges(List<CollectionEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public CollectionConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface CollectionEdgeQueryDefinition {
        void define(CollectionEdgeQuery _queryBuilder);
    }

    public static class CollectionEdgeQuery extends Query<CollectionEdgeQuery> {
        CollectionEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public CollectionEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of CollectionEdge.
        */
        public CollectionEdgeQuery node(CollectionQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new CollectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CollectionEdge extends AbstractResponse<CollectionEdge> {
        public CollectionEdge() {
        }

        public CollectionEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Collection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CollectionEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public CollectionEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of CollectionEdge.
        */

        public Collection getNode() {
            return (Collection) get("node");
        }

        public CollectionEdge setNode(Collection arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    /**
    * The set of valid sort keys for the collections query.
    */
    public enum CollectionSortKeys {
        ID,

        /**
        * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
        * the
        * results by relevance to the search term(s). When no search query is specified, this sort key is not
        * deterministic and should not be used.
        */
        RELEVANCE,

        TITLE,

        UPDATED_AT,

        UNKNOWN_VALUE;

        public static CollectionSortKeys fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "ID": {
                    return ID;
                }

                case "RELEVANCE": {
                    return RELEVANCE;
                }

                case "TITLE": {
                    return TITLE;
                }

                case "UPDATED_AT": {
                    return UPDATED_AT;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case ID: {
                    return "ID";
                }

                case RELEVANCE: {
                    return "RELEVANCE";
                }

                case TITLE: {
                    return "TITLE";
                }

                case UPDATED_AT: {
                    return "UPDATED_AT";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CommentQueryDefinition {
        void define(CommentQuery _queryBuilder);
    }

    public static class CommentQuery extends Query<CommentQuery> {
        CommentQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The comment’s author.
        */
        public CommentQuery author(CommentAuthorQueryDefinition queryDef) {
            startField("author");

            _queryBuilder.append('{');
            queryDef.define(new CommentAuthorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class ContentArguments extends Arguments {
            ContentArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncates string after the given length.
            */
            public ContentArguments truncateAt(Integer value) {
                if (value != null) {
                    startArgument("truncateAt");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ContentArgumentsDefinition {
            void define(ContentArguments args);
        }

        /**
        * Stripped content of the comment, single line with HTML tags removed.
        */
        public CommentQuery content() {
            return content(args -> {});
        }

        /**
        * Stripped content of the comment, single line with HTML tags removed.
        */
        public CommentQuery content(ContentArgumentsDefinition argsDef) {
            startField("content");

            ContentArguments args = new ContentArguments(_queryBuilder);
            argsDef.define(args);
            ContentArguments.end(args);

            return this;
        }

        /**
        * The content of the comment, complete with HTML formatting.
        */
        public CommentQuery contentHtml() {
            startField("contentHtml");

            return this;
        }
    }

    public static class Comment extends AbstractResponse<Comment> implements Node {
        public Comment() {
        }

        public Comment(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "author": {
                        responseData.put(key, new CommentAuthor(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "content": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "contentHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Comment(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Comment";
        }

        /**
        * The comment’s author.
        */

        public CommentAuthor getAuthor() {
            return (CommentAuthor) get("author");
        }

        public Comment setAuthor(CommentAuthor arg) {
            optimisticData.put(getKey("author"), arg);
            return this;
        }

        /**
        * Stripped content of the comment, single line with HTML tags removed.
        */

        public String getContent() {
            return (String) get("content");
        }

        public Comment setContent(String arg) {
            optimisticData.put(getKey("content"), arg);
            return this;
        }

        /**
        * The content of the comment, complete with HTML formatting.
        */

        public String getContentHtml() {
            return (String) get("contentHtml");
        }

        public Comment setContentHtml(String arg) {
            optimisticData.put(getKey("contentHtml"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "author": return true;

                case "content": return false;

                case "contentHtml": return false;

                case "id": return false;

                default: return false;
            }
        }
    }

    public interface CommentAuthorQueryDefinition {
        void define(CommentAuthorQuery _queryBuilder);
    }

    public static class CommentAuthorQuery extends Query<CommentAuthorQuery> {
        CommentAuthorQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The author's email.
        */
        public CommentAuthorQuery email() {
            startField("email");

            return this;
        }

        /**
        * The author’s name.
        */
        public CommentAuthorQuery name() {
            startField("name");

            return this;
        }
    }

    public static class CommentAuthor extends AbstractResponse<CommentAuthor> {
        public CommentAuthor() {
        }

        public CommentAuthor(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "email": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "name": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CommentAuthor";
        }

        /**
        * The author's email.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public CommentAuthor setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        /**
        * The author’s name.
        */

        public String getName() {
            return (String) get("name");
        }

        public CommentAuthor setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "email": return false;

                case "name": return false;

                default: return false;
            }
        }
    }

    public interface CommentConnectionQueryDefinition {
        void define(CommentConnectionQuery _queryBuilder);
    }

    public static class CommentConnectionQuery extends Query<CommentConnectionQuery> {
        CommentConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public CommentConnectionQuery edges(CommentEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new CommentEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public CommentConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CommentConnection extends AbstractResponse<CommentConnection> {
        public CommentConnection() {
        }

        public CommentConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<CommentEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new CommentEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CommentConnection";
        }

        /**
        * A list of edges.
        */

        public List<CommentEdge> getEdges() {
            return (List<CommentEdge>) get("edges");
        }

        public CommentConnection setEdges(List<CommentEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public CommentConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface CommentEdgeQueryDefinition {
        void define(CommentEdgeQuery _queryBuilder);
    }

    public static class CommentEdgeQuery extends Query<CommentEdgeQuery> {
        CommentEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public CommentEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of CommentEdge.
        */
        public CommentEdgeQuery node(CommentQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new CommentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CommentEdge extends AbstractResponse<CommentEdge> {
        public CommentEdge() {
        }

        public CommentEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Comment(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CommentEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public CommentEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of CommentEdge.
        */

        public Comment getNode() {
            return (Comment) get("node");
        }

        public CommentEdge setNode(Comment arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    /**
    * ISO 3166-1 alpha-2 country codes with some differences.
    */
    public enum CountryCode {
        /**
        * Andorra
        */
        AD,

        /**
        * United Arab Emirates
        */
        AE,

        /**
        * Afghanistan
        */
        AF,

        /**
        * Antigua And Barbuda
        */
        AG,

        /**
        * Anguilla
        */
        AI,

        /**
        * Albania
        */
        AL,

        /**
        * Armenia
        */
        AM,

        /**
        * Netherlands Antilles
        */
        AN,

        /**
        * Angola
        */
        AO,

        /**
        * Argentina
        */
        AR,

        /**
        * Austria
        */
        AT,

        /**
        * Australia
        */
        AU,

        /**
        * Aruba
        */
        AW,

        /**
        * Aland Islands
        */
        AX,

        /**
        * Azerbaijan
        */
        AZ,

        /**
        * Bosnia And Herzegovina
        */
        BA,

        /**
        * Barbados
        */
        BB,

        /**
        * Bangladesh
        */
        BD,

        /**
        * Belgium
        */
        BE,

        /**
        * Burkina Faso
        */
        BF,

        /**
        * Bulgaria
        */
        BG,

        /**
        * Bahrain
        */
        BH,

        /**
        * Burundi
        */
        BI,

        /**
        * Benin
        */
        BJ,

        /**
        * Saint Barthélemy
        */
        BL,

        /**
        * Bermuda
        */
        BM,

        /**
        * Brunei
        */
        BN,

        /**
        * Bolivia
        */
        BO,

        /**
        * Bonaire, Sint Eustatius and Saba
        */
        BQ,

        /**
        * Brazil
        */
        BR,

        /**
        * Bahamas
        */
        BS,

        /**
        * Bhutan
        */
        BT,

        /**
        * Bouvet Island
        */
        BV,

        /**
        * Botswana
        */
        BW,

        /**
        * Belarus
        */
        BY,

        /**
        * Belize
        */
        BZ,

        /**
        * Canada
        */
        CA,

        /**
        * Cocos (Keeling) Islands
        */
        CC,

        /**
        * Congo, The Democratic Republic Of The
        */
        CD,

        /**
        * Central African Republic
        */
        CF,

        /**
        * Congo
        */
        CG,

        /**
        * Switzerland
        */
        CH,

        /**
        * Côte d'Ivoire
        */
        CI,

        /**
        * Cook Islands
        */
        CK,

        /**
        * Chile
        */
        CL,

        /**
        * Republic of Cameroon
        */
        CM,

        /**
        * China
        */
        CN,

        /**
        * Colombia
        */
        CO,

        /**
        * Costa Rica
        */
        CR,

        /**
        * Cuba
        */
        CU,

        /**
        * Cape Verde
        */
        CV,

        /**
        * Curaçao
        */
        CW,

        /**
        * Christmas Island
        */
        CX,

        /**
        * Cyprus
        */
        CY,

        /**
        * Czech Republic
        */
        CZ,

        /**
        * Germany
        */
        DE,

        /**
        * Djibouti
        */
        DJ,

        /**
        * Denmark
        */
        DK,

        /**
        * Dominica
        */
        DM,

        /**
        * Dominican Republic
        */
        DO,

        /**
        * Algeria
        */
        DZ,

        /**
        * Ecuador
        */
        EC,

        /**
        * Estonia
        */
        EE,

        /**
        * Egypt
        */
        EG,

        /**
        * Western Sahara
        */
        EH,

        /**
        * Eritrea
        */
        ER,

        /**
        * Spain
        */
        ES,

        /**
        * Ethiopia
        */
        ET,

        /**
        * Finland
        */
        FI,

        /**
        * Fiji
        */
        FJ,

        /**
        * Falkland Islands (Malvinas)
        */
        FK,

        /**
        * Faroe Islands
        */
        FO,

        /**
        * France
        */
        FR,

        /**
        * Gabon
        */
        GA,

        /**
        * United Kingdom
        */
        GB,

        /**
        * Grenada
        */
        GD,

        /**
        * Georgia
        */
        GE,

        /**
        * French Guiana
        */
        GF,

        /**
        * Guernsey
        */
        GG,

        /**
        * Ghana
        */
        GH,

        /**
        * Gibraltar
        */
        GI,

        /**
        * Greenland
        */
        GL,

        /**
        * Gambia
        */
        GM,

        /**
        * Guinea
        */
        GN,

        /**
        * Guadeloupe
        */
        GP,

        /**
        * Equatorial Guinea
        */
        GQ,

        /**
        * Greece
        */
        GR,

        /**
        * South Georgia And The South Sandwich Islands
        */
        GS,

        /**
        * Guatemala
        */
        GT,

        /**
        * Guinea Bissau
        */
        GW,

        /**
        * Guyana
        */
        GY,

        /**
        * Hong Kong
        */
        HK,

        /**
        * Heard Island And Mcdonald Islands
        */
        HM,

        /**
        * Honduras
        */
        HN,

        /**
        * Croatia
        */
        HR,

        /**
        * Haiti
        */
        HT,

        /**
        * Hungary
        */
        HU,

        /**
        * Indonesia
        */
        ID,

        /**
        * Ireland
        */
        IE,

        /**
        * Israel
        */
        IL,

        /**
        * Isle Of Man
        */
        IM,

        /**
        * India
        */
        IN,

        /**
        * British Indian Ocean Territory
        */
        IO,

        /**
        * Iraq
        */
        IQ,

        /**
        * Iran, Islamic Republic Of
        */
        IR,

        /**
        * Iceland
        */
        IS,

        /**
        * Italy
        */
        IT,

        /**
        * Jersey
        */
        JE,

        /**
        * Jamaica
        */
        JM,

        /**
        * Jordan
        */
        JO,

        /**
        * Japan
        */
        JP,

        /**
        * Kenya
        */
        KE,

        /**
        * Kyrgyzstan
        */
        KG,

        /**
        * Cambodia
        */
        KH,

        /**
        * Kiribati
        */
        KI,

        /**
        * Comoros
        */
        KM,

        /**
        * Saint Kitts And Nevis
        */
        KN,

        /**
        * Korea, Democratic People's Republic Of
        */
        KP,

        /**
        * South Korea
        */
        KR,

        /**
        * Kuwait
        */
        KW,

        /**
        * Cayman Islands
        */
        KY,

        /**
        * Kazakhstan
        */
        KZ,

        /**
        * Lao People's Democratic Republic
        */
        LA,

        /**
        * Lebanon
        */
        LB,

        /**
        * Saint Lucia
        */
        LC,

        /**
        * Liechtenstein
        */
        LI,

        /**
        * Sri Lanka
        */
        LK,

        /**
        * Liberia
        */
        LR,

        /**
        * Lesotho
        */
        LS,

        /**
        * Lithuania
        */
        LT,

        /**
        * Luxembourg
        */
        LU,

        /**
        * Latvia
        */
        LV,

        /**
        * Libyan Arab Jamahiriya
        */
        LY,

        /**
        * Morocco
        */
        MA,

        /**
        * Monaco
        */
        MC,

        /**
        * Moldova, Republic of
        */
        MD,

        /**
        * Montenegro
        */
        ME,

        /**
        * Saint Martin
        */
        MF,

        /**
        * Madagascar
        */
        MG,

        /**
        * Macedonia, Republic Of
        */
        MK,

        /**
        * Mali
        */
        ML,

        /**
        * Myanmar
        */
        MM,

        /**
        * Mongolia
        */
        MN,

        /**
        * Macao
        */
        MO,

        /**
        * Martinique
        */
        MQ,

        /**
        * Mauritania
        */
        MR,

        /**
        * Montserrat
        */
        MS,

        /**
        * Malta
        */
        MT,

        /**
        * Mauritius
        */
        MU,

        /**
        * Maldives
        */
        MV,

        /**
        * Malawi
        */
        MW,

        /**
        * Mexico
        */
        MX,

        /**
        * Malaysia
        */
        MY,

        /**
        * Mozambique
        */
        MZ,

        /**
        * Namibia
        */
        NA,

        /**
        * New Caledonia
        */
        NC,

        /**
        * Niger
        */
        NE,

        /**
        * Norfolk Island
        */
        NF,

        /**
        * Nigeria
        */
        NG,

        /**
        * Nicaragua
        */
        NI,

        /**
        * Netherlands
        */
        NL,

        /**
        * Norway
        */
        NO,

        /**
        * Nepal
        */
        NP,

        /**
        * Nauru
        */
        NR,

        /**
        * Niue
        */
        NU,

        /**
        * New Zealand
        */
        NZ,

        /**
        * Oman
        */
        OM,

        /**
        * Panama
        */
        PA,

        /**
        * Peru
        */
        PE,

        /**
        * French Polynesia
        */
        PF,

        /**
        * Papua New Guinea
        */
        PG,

        /**
        * Philippines
        */
        PH,

        /**
        * Pakistan
        */
        PK,

        /**
        * Poland
        */
        PL,

        /**
        * Saint Pierre And Miquelon
        */
        PM,

        /**
        * Pitcairn
        */
        PN,

        /**
        * Palestinian Territory, Occupied
        */
        PS,

        /**
        * Portugal
        */
        PT,

        /**
        * Paraguay
        */
        PY,

        /**
        * Qatar
        */
        QA,

        /**
        * Reunion
        */
        RE,

        /**
        * Romania
        */
        RO,

        /**
        * Serbia
        */
        RS,

        /**
        * Russia
        */
        RU,

        /**
        * Rwanda
        */
        RW,

        /**
        * Saudi Arabia
        */
        SA,

        /**
        * Solomon Islands
        */
        SB,

        /**
        * Seychelles
        */
        SC,

        /**
        * Sudan
        */
        SD,

        /**
        * Sweden
        */
        SE,

        /**
        * Singapore
        */
        SG,

        /**
        * Saint Helena
        */
        SH,

        /**
        * Slovenia
        */
        SI,

        /**
        * Svalbard And Jan Mayen
        */
        SJ,

        /**
        * Slovakia
        */
        SK,

        /**
        * Sierra Leone
        */
        SL,

        /**
        * San Marino
        */
        SM,

        /**
        * Senegal
        */
        SN,

        /**
        * Somalia
        */
        SO,

        /**
        * Suriname
        */
        SR,

        /**
        * South Sudan
        */
        SS,

        /**
        * Sao Tome And Principe
        */
        ST,

        /**
        * El Salvador
        */
        SV,

        /**
        * Sint Maarten
        */
        SX,

        /**
        * Syria
        */
        SY,

        /**
        * Swaziland
        */
        SZ,

        /**
        * Turks and Caicos Islands
        */
        TC,

        /**
        * Chad
        */
        TD,

        /**
        * French Southern Territories
        */
        TF,

        /**
        * Togo
        */
        TG,

        /**
        * Thailand
        */
        TH,

        /**
        * Tajikistan
        */
        TJ,

        /**
        * Tokelau
        */
        TK,

        /**
        * Timor Leste
        */
        TL,

        /**
        * Turkmenistan
        */
        TM,

        /**
        * Tunisia
        */
        TN,

        /**
        * Tonga
        */
        TO,

        /**
        * Turkey
        */
        TR,

        /**
        * Trinidad and Tobago
        */
        TT,

        /**
        * Tuvalu
        */
        TV,

        /**
        * Taiwan
        */
        TW,

        /**
        * Tanzania, United Republic Of
        */
        TZ,

        /**
        * Ukraine
        */
        UA,

        /**
        * Uganda
        */
        UG,

        /**
        * United States Minor Outlying Islands
        */
        UM,

        /**
        * United States
        */
        US,

        /**
        * Uruguay
        */
        UY,

        /**
        * Uzbekistan
        */
        UZ,

        /**
        * Holy See (Vatican City State)
        */
        VA,

        /**
        * St. Vincent
        */
        VC,

        /**
        * Venezuela
        */
        VE,

        /**
        * Virgin Islands, British
        */
        VG,

        /**
        * Vietnam
        */
        VN,

        /**
        * Vanuatu
        */
        VU,

        /**
        * Wallis And Futuna
        */
        WF,

        /**
        * Samoa
        */
        WS,

        /**
        * Kosovo
        */
        XK,

        /**
        * Yemen
        */
        YE,

        /**
        * Mayotte
        */
        YT,

        /**
        * South Africa
        */
        ZA,

        /**
        * Zambia
        */
        ZM,

        /**
        * Zimbabwe
        */
        ZW,

        UNKNOWN_VALUE;

        public static CountryCode fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AD": {
                    return AD;
                }

                case "AE": {
                    return AE;
                }

                case "AF": {
                    return AF;
                }

                case "AG": {
                    return AG;
                }

                case "AI": {
                    return AI;
                }

                case "AL": {
                    return AL;
                }

                case "AM": {
                    return AM;
                }

                case "AN": {
                    return AN;
                }

                case "AO": {
                    return AO;
                }

                case "AR": {
                    return AR;
                }

                case "AT": {
                    return AT;
                }

                case "AU": {
                    return AU;
                }

                case "AW": {
                    return AW;
                }

                case "AX": {
                    return AX;
                }

                case "AZ": {
                    return AZ;
                }

                case "BA": {
                    return BA;
                }

                case "BB": {
                    return BB;
                }

                case "BD": {
                    return BD;
                }

                case "BE": {
                    return BE;
                }

                case "BF": {
                    return BF;
                }

                case "BG": {
                    return BG;
                }

                case "BH": {
                    return BH;
                }

                case "BI": {
                    return BI;
                }

                case "BJ": {
                    return BJ;
                }

                case "BL": {
                    return BL;
                }

                case "BM": {
                    return BM;
                }

                case "BN": {
                    return BN;
                }

                case "BO": {
                    return BO;
                }

                case "BQ": {
                    return BQ;
                }

                case "BR": {
                    return BR;
                }

                case "BS": {
                    return BS;
                }

                case "BT": {
                    return BT;
                }

                case "BV": {
                    return BV;
                }

                case "BW": {
                    return BW;
                }

                case "BY": {
                    return BY;
                }

                case "BZ": {
                    return BZ;
                }

                case "CA": {
                    return CA;
                }

                case "CC": {
                    return CC;
                }

                case "CD": {
                    return CD;
                }

                case "CF": {
                    return CF;
                }

                case "CG": {
                    return CG;
                }

                case "CH": {
                    return CH;
                }

                case "CI": {
                    return CI;
                }

                case "CK": {
                    return CK;
                }

                case "CL": {
                    return CL;
                }

                case "CM": {
                    return CM;
                }

                case "CN": {
                    return CN;
                }

                case "CO": {
                    return CO;
                }

                case "CR": {
                    return CR;
                }

                case "CU": {
                    return CU;
                }

                case "CV": {
                    return CV;
                }

                case "CW": {
                    return CW;
                }

                case "CX": {
                    return CX;
                }

                case "CY": {
                    return CY;
                }

                case "CZ": {
                    return CZ;
                }

                case "DE": {
                    return DE;
                }

                case "DJ": {
                    return DJ;
                }

                case "DK": {
                    return DK;
                }

                case "DM": {
                    return DM;
                }

                case "DO": {
                    return DO;
                }

                case "DZ": {
                    return DZ;
                }

                case "EC": {
                    return EC;
                }

                case "EE": {
                    return EE;
                }

                case "EG": {
                    return EG;
                }

                case "EH": {
                    return EH;
                }

                case "ER": {
                    return ER;
                }

                case "ES": {
                    return ES;
                }

                case "ET": {
                    return ET;
                }

                case "FI": {
                    return FI;
                }

                case "FJ": {
                    return FJ;
                }

                case "FK": {
                    return FK;
                }

                case "FO": {
                    return FO;
                }

                case "FR": {
                    return FR;
                }

                case "GA": {
                    return GA;
                }

                case "GB": {
                    return GB;
                }

                case "GD": {
                    return GD;
                }

                case "GE": {
                    return GE;
                }

                case "GF": {
                    return GF;
                }

                case "GG": {
                    return GG;
                }

                case "GH": {
                    return GH;
                }

                case "GI": {
                    return GI;
                }

                case "GL": {
                    return GL;
                }

                case "GM": {
                    return GM;
                }

                case "GN": {
                    return GN;
                }

                case "GP": {
                    return GP;
                }

                case "GQ": {
                    return GQ;
                }

                case "GR": {
                    return GR;
                }

                case "GS": {
                    return GS;
                }

                case "GT": {
                    return GT;
                }

                case "GW": {
                    return GW;
                }

                case "GY": {
                    return GY;
                }

                case "HK": {
                    return HK;
                }

                case "HM": {
                    return HM;
                }

                case "HN": {
                    return HN;
                }

                case "HR": {
                    return HR;
                }

                case "HT": {
                    return HT;
                }

                case "HU": {
                    return HU;
                }

                case "ID": {
                    return ID;
                }

                case "IE": {
                    return IE;
                }

                case "IL": {
                    return IL;
                }

                case "IM": {
                    return IM;
                }

                case "IN": {
                    return IN;
                }

                case "IO": {
                    return IO;
                }

                case "IQ": {
                    return IQ;
                }

                case "IR": {
                    return IR;
                }

                case "IS": {
                    return IS;
                }

                case "IT": {
                    return IT;
                }

                case "JE": {
                    return JE;
                }

                case "JM": {
                    return JM;
                }

                case "JO": {
                    return JO;
                }

                case "JP": {
                    return JP;
                }

                case "KE": {
                    return KE;
                }

                case "KG": {
                    return KG;
                }

                case "KH": {
                    return KH;
                }

                case "KI": {
                    return KI;
                }

                case "KM": {
                    return KM;
                }

                case "KN": {
                    return KN;
                }

                case "KP": {
                    return KP;
                }

                case "KR": {
                    return KR;
                }

                case "KW": {
                    return KW;
                }

                case "KY": {
                    return KY;
                }

                case "KZ": {
                    return KZ;
                }

                case "LA": {
                    return LA;
                }

                case "LB": {
                    return LB;
                }

                case "LC": {
                    return LC;
                }

                case "LI": {
                    return LI;
                }

                case "LK": {
                    return LK;
                }

                case "LR": {
                    return LR;
                }

                case "LS": {
                    return LS;
                }

                case "LT": {
                    return LT;
                }

                case "LU": {
                    return LU;
                }

                case "LV": {
                    return LV;
                }

                case "LY": {
                    return LY;
                }

                case "MA": {
                    return MA;
                }

                case "MC": {
                    return MC;
                }

                case "MD": {
                    return MD;
                }

                case "ME": {
                    return ME;
                }

                case "MF": {
                    return MF;
                }

                case "MG": {
                    return MG;
                }

                case "MK": {
                    return MK;
                }

                case "ML": {
                    return ML;
                }

                case "MM": {
                    return MM;
                }

                case "MN": {
                    return MN;
                }

                case "MO": {
                    return MO;
                }

                case "MQ": {
                    return MQ;
                }

                case "MR": {
                    return MR;
                }

                case "MS": {
                    return MS;
                }

                case "MT": {
                    return MT;
                }

                case "MU": {
                    return MU;
                }

                case "MV": {
                    return MV;
                }

                case "MW": {
                    return MW;
                }

                case "MX": {
                    return MX;
                }

                case "MY": {
                    return MY;
                }

                case "MZ": {
                    return MZ;
                }

                case "NA": {
                    return NA;
                }

                case "NC": {
                    return NC;
                }

                case "NE": {
                    return NE;
                }

                case "NF": {
                    return NF;
                }

                case "NG": {
                    return NG;
                }

                case "NI": {
                    return NI;
                }

                case "NL": {
                    return NL;
                }

                case "NO": {
                    return NO;
                }

                case "NP": {
                    return NP;
                }

                case "NR": {
                    return NR;
                }

                case "NU": {
                    return NU;
                }

                case "NZ": {
                    return NZ;
                }

                case "OM": {
                    return OM;
                }

                case "PA": {
                    return PA;
                }

                case "PE": {
                    return PE;
                }

                case "PF": {
                    return PF;
                }

                case "PG": {
                    return PG;
                }

                case "PH": {
                    return PH;
                }

                case "PK": {
                    return PK;
                }

                case "PL": {
                    return PL;
                }

                case "PM": {
                    return PM;
                }

                case "PN": {
                    return PN;
                }

                case "PS": {
                    return PS;
                }

                case "PT": {
                    return PT;
                }

                case "PY": {
                    return PY;
                }

                case "QA": {
                    return QA;
                }

                case "RE": {
                    return RE;
                }

                case "RO": {
                    return RO;
                }

                case "RS": {
                    return RS;
                }

                case "RU": {
                    return RU;
                }

                case "RW": {
                    return RW;
                }

                case "SA": {
                    return SA;
                }

                case "SB": {
                    return SB;
                }

                case "SC": {
                    return SC;
                }

                case "SD": {
                    return SD;
                }

                case "SE": {
                    return SE;
                }

                case "SG": {
                    return SG;
                }

                case "SH": {
                    return SH;
                }

                case "SI": {
                    return SI;
                }

                case "SJ": {
                    return SJ;
                }

                case "SK": {
                    return SK;
                }

                case "SL": {
                    return SL;
                }

                case "SM": {
                    return SM;
                }

                case "SN": {
                    return SN;
                }

                case "SO": {
                    return SO;
                }

                case "SR": {
                    return SR;
                }

                case "SS": {
                    return SS;
                }

                case "ST": {
                    return ST;
                }

                case "SV": {
                    return SV;
                }

                case "SX": {
                    return SX;
                }

                case "SY": {
                    return SY;
                }

                case "SZ": {
                    return SZ;
                }

                case "TC": {
                    return TC;
                }

                case "TD": {
                    return TD;
                }

                case "TF": {
                    return TF;
                }

                case "TG": {
                    return TG;
                }

                case "TH": {
                    return TH;
                }

                case "TJ": {
                    return TJ;
                }

                case "TK": {
                    return TK;
                }

                case "TL": {
                    return TL;
                }

                case "TM": {
                    return TM;
                }

                case "TN": {
                    return TN;
                }

                case "TO": {
                    return TO;
                }

                case "TR": {
                    return TR;
                }

                case "TT": {
                    return TT;
                }

                case "TV": {
                    return TV;
                }

                case "TW": {
                    return TW;
                }

                case "TZ": {
                    return TZ;
                }

                case "UA": {
                    return UA;
                }

                case "UG": {
                    return UG;
                }

                case "UM": {
                    return UM;
                }

                case "US": {
                    return US;
                }

                case "UY": {
                    return UY;
                }

                case "UZ": {
                    return UZ;
                }

                case "VA": {
                    return VA;
                }

                case "VC": {
                    return VC;
                }

                case "VE": {
                    return VE;
                }

                case "VG": {
                    return VG;
                }

                case "VN": {
                    return VN;
                }

                case "VU": {
                    return VU;
                }

                case "WF": {
                    return WF;
                }

                case "WS": {
                    return WS;
                }

                case "XK": {
                    return XK;
                }

                case "YE": {
                    return YE;
                }

                case "YT": {
                    return YT;
                }

                case "ZA": {
                    return ZA;
                }

                case "ZM": {
                    return ZM;
                }

                case "ZW": {
                    return ZW;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AD: {
                    return "AD";
                }

                case AE: {
                    return "AE";
                }

                case AF: {
                    return "AF";
                }

                case AG: {
                    return "AG";
                }

                case AI: {
                    return "AI";
                }

                case AL: {
                    return "AL";
                }

                case AM: {
                    return "AM";
                }

                case AN: {
                    return "AN";
                }

                case AO: {
                    return "AO";
                }

                case AR: {
                    return "AR";
                }

                case AT: {
                    return "AT";
                }

                case AU: {
                    return "AU";
                }

                case AW: {
                    return "AW";
                }

                case AX: {
                    return "AX";
                }

                case AZ: {
                    return "AZ";
                }

                case BA: {
                    return "BA";
                }

                case BB: {
                    return "BB";
                }

                case BD: {
                    return "BD";
                }

                case BE: {
                    return "BE";
                }

                case BF: {
                    return "BF";
                }

                case BG: {
                    return "BG";
                }

                case BH: {
                    return "BH";
                }

                case BI: {
                    return "BI";
                }

                case BJ: {
                    return "BJ";
                }

                case BL: {
                    return "BL";
                }

                case BM: {
                    return "BM";
                }

                case BN: {
                    return "BN";
                }

                case BO: {
                    return "BO";
                }

                case BQ: {
                    return "BQ";
                }

                case BR: {
                    return "BR";
                }

                case BS: {
                    return "BS";
                }

                case BT: {
                    return "BT";
                }

                case BV: {
                    return "BV";
                }

                case BW: {
                    return "BW";
                }

                case BY: {
                    return "BY";
                }

                case BZ: {
                    return "BZ";
                }

                case CA: {
                    return "CA";
                }

                case CC: {
                    return "CC";
                }

                case CD: {
                    return "CD";
                }

                case CF: {
                    return "CF";
                }

                case CG: {
                    return "CG";
                }

                case CH: {
                    return "CH";
                }

                case CI: {
                    return "CI";
                }

                case CK: {
                    return "CK";
                }

                case CL: {
                    return "CL";
                }

                case CM: {
                    return "CM";
                }

                case CN: {
                    return "CN";
                }

                case CO: {
                    return "CO";
                }

                case CR: {
                    return "CR";
                }

                case CU: {
                    return "CU";
                }

                case CV: {
                    return "CV";
                }

                case CW: {
                    return "CW";
                }

                case CX: {
                    return "CX";
                }

                case CY: {
                    return "CY";
                }

                case CZ: {
                    return "CZ";
                }

                case DE: {
                    return "DE";
                }

                case DJ: {
                    return "DJ";
                }

                case DK: {
                    return "DK";
                }

                case DM: {
                    return "DM";
                }

                case DO: {
                    return "DO";
                }

                case DZ: {
                    return "DZ";
                }

                case EC: {
                    return "EC";
                }

                case EE: {
                    return "EE";
                }

                case EG: {
                    return "EG";
                }

                case EH: {
                    return "EH";
                }

                case ER: {
                    return "ER";
                }

                case ES: {
                    return "ES";
                }

                case ET: {
                    return "ET";
                }

                case FI: {
                    return "FI";
                }

                case FJ: {
                    return "FJ";
                }

                case FK: {
                    return "FK";
                }

                case FO: {
                    return "FO";
                }

                case FR: {
                    return "FR";
                }

                case GA: {
                    return "GA";
                }

                case GB: {
                    return "GB";
                }

                case GD: {
                    return "GD";
                }

                case GE: {
                    return "GE";
                }

                case GF: {
                    return "GF";
                }

                case GG: {
                    return "GG";
                }

                case GH: {
                    return "GH";
                }

                case GI: {
                    return "GI";
                }

                case GL: {
                    return "GL";
                }

                case GM: {
                    return "GM";
                }

                case GN: {
                    return "GN";
                }

                case GP: {
                    return "GP";
                }

                case GQ: {
                    return "GQ";
                }

                case GR: {
                    return "GR";
                }

                case GS: {
                    return "GS";
                }

                case GT: {
                    return "GT";
                }

                case GW: {
                    return "GW";
                }

                case GY: {
                    return "GY";
                }

                case HK: {
                    return "HK";
                }

                case HM: {
                    return "HM";
                }

                case HN: {
                    return "HN";
                }

                case HR: {
                    return "HR";
                }

                case HT: {
                    return "HT";
                }

                case HU: {
                    return "HU";
                }

                case ID: {
                    return "ID";
                }

                case IE: {
                    return "IE";
                }

                case IL: {
                    return "IL";
                }

                case IM: {
                    return "IM";
                }

                case IN: {
                    return "IN";
                }

                case IO: {
                    return "IO";
                }

                case IQ: {
                    return "IQ";
                }

                case IR: {
                    return "IR";
                }

                case IS: {
                    return "IS";
                }

                case IT: {
                    return "IT";
                }

                case JE: {
                    return "JE";
                }

                case JM: {
                    return "JM";
                }

                case JO: {
                    return "JO";
                }

                case JP: {
                    return "JP";
                }

                case KE: {
                    return "KE";
                }

                case KG: {
                    return "KG";
                }

                case KH: {
                    return "KH";
                }

                case KI: {
                    return "KI";
                }

                case KM: {
                    return "KM";
                }

                case KN: {
                    return "KN";
                }

                case KP: {
                    return "KP";
                }

                case KR: {
                    return "KR";
                }

                case KW: {
                    return "KW";
                }

                case KY: {
                    return "KY";
                }

                case KZ: {
                    return "KZ";
                }

                case LA: {
                    return "LA";
                }

                case LB: {
                    return "LB";
                }

                case LC: {
                    return "LC";
                }

                case LI: {
                    return "LI";
                }

                case LK: {
                    return "LK";
                }

                case LR: {
                    return "LR";
                }

                case LS: {
                    return "LS";
                }

                case LT: {
                    return "LT";
                }

                case LU: {
                    return "LU";
                }

                case LV: {
                    return "LV";
                }

                case LY: {
                    return "LY";
                }

                case MA: {
                    return "MA";
                }

                case MC: {
                    return "MC";
                }

                case MD: {
                    return "MD";
                }

                case ME: {
                    return "ME";
                }

                case MF: {
                    return "MF";
                }

                case MG: {
                    return "MG";
                }

                case MK: {
                    return "MK";
                }

                case ML: {
                    return "ML";
                }

                case MM: {
                    return "MM";
                }

                case MN: {
                    return "MN";
                }

                case MO: {
                    return "MO";
                }

                case MQ: {
                    return "MQ";
                }

                case MR: {
                    return "MR";
                }

                case MS: {
                    return "MS";
                }

                case MT: {
                    return "MT";
                }

                case MU: {
                    return "MU";
                }

                case MV: {
                    return "MV";
                }

                case MW: {
                    return "MW";
                }

                case MX: {
                    return "MX";
                }

                case MY: {
                    return "MY";
                }

                case MZ: {
                    return "MZ";
                }

                case NA: {
                    return "NA";
                }

                case NC: {
                    return "NC";
                }

                case NE: {
                    return "NE";
                }

                case NF: {
                    return "NF";
                }

                case NG: {
                    return "NG";
                }

                case NI: {
                    return "NI";
                }

                case NL: {
                    return "NL";
                }

                case NO: {
                    return "NO";
                }

                case NP: {
                    return "NP";
                }

                case NR: {
                    return "NR";
                }

                case NU: {
                    return "NU";
                }

                case NZ: {
                    return "NZ";
                }

                case OM: {
                    return "OM";
                }

                case PA: {
                    return "PA";
                }

                case PE: {
                    return "PE";
                }

                case PF: {
                    return "PF";
                }

                case PG: {
                    return "PG";
                }

                case PH: {
                    return "PH";
                }

                case PK: {
                    return "PK";
                }

                case PL: {
                    return "PL";
                }

                case PM: {
                    return "PM";
                }

                case PN: {
                    return "PN";
                }

                case PS: {
                    return "PS";
                }

                case PT: {
                    return "PT";
                }

                case PY: {
                    return "PY";
                }

                case QA: {
                    return "QA";
                }

                case RE: {
                    return "RE";
                }

                case RO: {
                    return "RO";
                }

                case RS: {
                    return "RS";
                }

                case RU: {
                    return "RU";
                }

                case RW: {
                    return "RW";
                }

                case SA: {
                    return "SA";
                }

                case SB: {
                    return "SB";
                }

                case SC: {
                    return "SC";
                }

                case SD: {
                    return "SD";
                }

                case SE: {
                    return "SE";
                }

                case SG: {
                    return "SG";
                }

                case SH: {
                    return "SH";
                }

                case SI: {
                    return "SI";
                }

                case SJ: {
                    return "SJ";
                }

                case SK: {
                    return "SK";
                }

                case SL: {
                    return "SL";
                }

                case SM: {
                    return "SM";
                }

                case SN: {
                    return "SN";
                }

                case SO: {
                    return "SO";
                }

                case SR: {
                    return "SR";
                }

                case SS: {
                    return "SS";
                }

                case ST: {
                    return "ST";
                }

                case SV: {
                    return "SV";
                }

                case SX: {
                    return "SX";
                }

                case SY: {
                    return "SY";
                }

                case SZ: {
                    return "SZ";
                }

                case TC: {
                    return "TC";
                }

                case TD: {
                    return "TD";
                }

                case TF: {
                    return "TF";
                }

                case TG: {
                    return "TG";
                }

                case TH: {
                    return "TH";
                }

                case TJ: {
                    return "TJ";
                }

                case TK: {
                    return "TK";
                }

                case TL: {
                    return "TL";
                }

                case TM: {
                    return "TM";
                }

                case TN: {
                    return "TN";
                }

                case TO: {
                    return "TO";
                }

                case TR: {
                    return "TR";
                }

                case TT: {
                    return "TT";
                }

                case TV: {
                    return "TV";
                }

                case TW: {
                    return "TW";
                }

                case TZ: {
                    return "TZ";
                }

                case UA: {
                    return "UA";
                }

                case UG: {
                    return "UG";
                }

                case UM: {
                    return "UM";
                }

                case US: {
                    return "US";
                }

                case UY: {
                    return "UY";
                }

                case UZ: {
                    return "UZ";
                }

                case VA: {
                    return "VA";
                }

                case VC: {
                    return "VC";
                }

                case VE: {
                    return "VE";
                }

                case VG: {
                    return "VG";
                }

                case VN: {
                    return "VN";
                }

                case VU: {
                    return "VU";
                }

                case WF: {
                    return "WF";
                }

                case WS: {
                    return "WS";
                }

                case XK: {
                    return "XK";
                }

                case YE: {
                    return "YE";
                }

                case YT: {
                    return "YT";
                }

                case ZA: {
                    return "ZA";
                }

                case ZM: {
                    return "ZM";
                }

                case ZW: {
                    return "ZW";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CreditCardQueryDefinition {
        void define(CreditCardQuery _queryBuilder);
    }

    /**
    * Credit card information used for a payment.
    */
    public static class CreditCardQuery extends Query<CreditCardQuery> {
        CreditCardQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CreditCardQuery brand() {
            startField("brand");

            return this;
        }

        public CreditCardQuery expiryMonth() {
            startField("expiryMonth");

            return this;
        }

        public CreditCardQuery expiryYear() {
            startField("expiryYear");

            return this;
        }

        public CreditCardQuery firstDigits() {
            startField("firstDigits");

            return this;
        }

        public CreditCardQuery firstName() {
            startField("firstName");

            return this;
        }

        public CreditCardQuery lastDigits() {
            startField("lastDigits");

            return this;
        }

        public CreditCardQuery lastName() {
            startField("lastName");

            return this;
        }

        /**
        * Masked credit card number with only the last 4 digits displayed
        */
        public CreditCardQuery maskedNumber() {
            startField("maskedNumber");

            return this;
        }
    }

    /**
    * Credit card information used for a payment.
    */
    public static class CreditCard extends AbstractResponse<CreditCard> {
        public CreditCard() {
        }

        public CreditCard(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "brand": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "expiryMonth": {
                        Integer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsInteger(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "expiryYear": {
                        Integer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsInteger(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstDigits": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "lastDigits": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "lastName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "maskedNumber": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CreditCard";
        }

        public String getBrand() {
            return (String) get("brand");
        }

        public CreditCard setBrand(String arg) {
            optimisticData.put(getKey("brand"), arg);
            return this;
        }

        public Integer getExpiryMonth() {
            return (Integer) get("expiryMonth");
        }

        public CreditCard setExpiryMonth(Integer arg) {
            optimisticData.put(getKey("expiryMonth"), arg);
            return this;
        }

        public Integer getExpiryYear() {
            return (Integer) get("expiryYear");
        }

        public CreditCard setExpiryYear(Integer arg) {
            optimisticData.put(getKey("expiryYear"), arg);
            return this;
        }

        public String getFirstDigits() {
            return (String) get("firstDigits");
        }

        public CreditCard setFirstDigits(String arg) {
            optimisticData.put(getKey("firstDigits"), arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public CreditCard setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        public String getLastDigits() {
            return (String) get("lastDigits");
        }

        public CreditCard setLastDigits(String arg) {
            optimisticData.put(getKey("lastDigits"), arg);
            return this;
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public CreditCard setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        /**
        * Masked credit card number with only the last 4 digits displayed
        */

        public String getMaskedNumber() {
            return (String) get("maskedNumber");
        }

        public CreditCard setMaskedNumber(String arg) {
            optimisticData.put(getKey("maskedNumber"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "brand": return false;

                case "expiryMonth": return false;

                case "expiryYear": return false;

                case "firstDigits": return false;

                case "firstName": return false;

                case "lastDigits": return false;

                case "lastName": return false;

                case "maskedNumber": return false;

                default: return false;
            }
        }
    }

    public static class CreditCardPaymentInput implements Serializable {
        private BigDecimal amount;

        private String idempotencyKey;

        private MailingAddressInput billingAddress;

        private String vaultId;

        private Input<Boolean> test = Input.undefined();

        public CreditCardPaymentInput(BigDecimal amount, String idempotencyKey, MailingAddressInput billingAddress, String vaultId) {
            this.amount = amount;

            this.idempotencyKey = idempotencyKey;

            this.billingAddress = billingAddress;

            this.vaultId = vaultId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public CreditCardPaymentInput setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public String getIdempotencyKey() {
            return idempotencyKey;
        }

        public CreditCardPaymentInput setIdempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public MailingAddressInput getBillingAddress() {
            return billingAddress;
        }

        public CreditCardPaymentInput setBillingAddress(MailingAddressInput billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public String getVaultId() {
            return vaultId;
        }

        public CreditCardPaymentInput setVaultId(String vaultId) {
            this.vaultId = vaultId;
            return this;
        }

        public Boolean getTest() {
            return test.getValue();
        }

        public Input<Boolean> getTestInput() {
            return test;
        }

        public CreditCardPaymentInput setTest(Boolean test) {
            this.test = Input.optional(test);
            return this;
        }

        public CreditCardPaymentInput setTestInput(Input<Boolean> test) {
            if (test == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.test = test;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("amount:");
            Query.appendQuotedString(_queryBuilder, amount.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("idempotencyKey:");
            Query.appendQuotedString(_queryBuilder, idempotencyKey.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("billingAddress:");
            billingAddress.appendTo(_queryBuilder);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("vaultId:");
            Query.appendQuotedString(_queryBuilder, vaultId.toString());

            if (this.test.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("test:");
                if (test.getValue() != null) {
                    _queryBuilder.append(test.getValue());
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    /**
    * The part of the image that should remain after cropping.
    */
    public enum CropRegion {
        /**
        * Keep the bottom of the image
        */
        BOTTOM,

        /**
        * Keep the center of the image
        */
        CENTER,

        /**
        * Keep the left of the image
        */
        LEFT,

        /**
        * Keep the right of the image
        */
        RIGHT,

        /**
        * Keep the top of the image
        */
        TOP,

        UNKNOWN_VALUE;

        public static CropRegion fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "BOTTOM": {
                    return BOTTOM;
                }

                case "CENTER": {
                    return CENTER;
                }

                case "LEFT": {
                    return LEFT;
                }

                case "RIGHT": {
                    return RIGHT;
                }

                case "TOP": {
                    return TOP;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case BOTTOM: {
                    return "BOTTOM";
                }

                case CENTER: {
                    return "CENTER";
                }

                case LEFT: {
                    return "LEFT";
                }

                case RIGHT: {
                    return "RIGHT";
                }

                case TOP: {
                    return "TOP";
                }

                default: {
                    return "";
                }
            }
        }
    }

    /**
    * Currency codes
    */
    public enum CurrencyCode {
        /**
        * United Arab Emirates Dirham (AED)
        */
        AED,

        /**
        * Afghan Afghani (AFN)
        */
        AFN,

        /**
        * Albanian Lek (ALL)
        */
        ALL,

        /**
        * Armenian Dram (AMD)
        */
        AMD,

        /**
        * Netherlands Antillean Guilder
        */
        ANG,

        /**
        * Angolan Kwanza (AOA)
        */
        AOA,

        /**
        * Argentine Pesos (ARS)
        */
        ARS,

        /**
        * Australian Dollars (AUD)
        */
        AUD,

        /**
        * Aruban Florin (AWG)
        */
        AWG,

        /**
        * Azerbaijani Manat (AZN)
        */
        AZN,

        /**
        * Bosnia and Herzegovina Convertible Mark (BAM)
        */
        BAM,

        /**
        * Barbadian Dollar (BBD)
        */
        BBD,

        /**
        * Bangladesh Taka (BDT)
        */
        BDT,

        /**
        * Bulgarian Lev (BGN)
        */
        BGN,

        /**
        * Bahraini Dinar (BHD)
        */
        BHD,

        /**
        * Burundian Franc (BIF)
        */
        BIF,

        /**
        * Brunei Dollar (BND)
        */
        BND,

        /**
        * Bolivian Boliviano (BOB)
        */
        BOB,

        /**
        * Brazilian Real (BRL)
        */
        BRL,

        /**
        * Bahamian Dollar (BSD)
        */
        BSD,

        /**
        * Bhutanese Ngultrum (BTN)
        */
        BTN,

        /**
        * Botswana Pula (BWP)
        */
        BWP,

        /**
        * Belarusian Ruble (BYR)
        */
        BYR,

        /**
        * Belize Dollar (BZD)
        */
        BZD,

        /**
        * Canadian Dollars (CAD)
        */
        CAD,

        /**
        * Congolese franc (CDF)
        */
        CDF,

        /**
        * Swiss Francs (CHF)
        */
        CHF,

        /**
        * Chilean Peso (CLP)
        */
        CLP,

        /**
        * Chinese Yuan Renminbi (CNY)
        */
        CNY,

        /**
        * Colombian Peso (COP)
        */
        COP,

        /**
        * Costa Rican Colones (CRC)
        */
        CRC,

        /**
        * Cape Verdean escudo (CVE)
        */
        CVE,

        /**
        * Czech Koruny (CZK)
        */
        CZK,

        /**
        * Danish Kroner (DKK)
        */
        DKK,

        /**
        * Dominican Peso (DOP)
        */
        DOP,

        /**
        * Algerian Dinar (DZD)
        */
        DZD,

        /**
        * Egyptian Pound (EGP)
        */
        EGP,

        /**
        * Ethiopian Birr (ETB)
        */
        ETB,

        /**
        * Euro (EUR)
        */
        EUR,

        /**
        * Fijian Dollars (FJD)
        */
        FJD,

        /**
        * United Kingdom Pounds (GBP)
        */
        GBP,

        /**
        * Georgian Lari (GEL)
        */
        GEL,

        /**
        * Ghanaian Cedi (GHS)
        */
        GHS,

        /**
        * Gambian Dalasi (GMD)
        */
        GMD,

        /**
        * Guatemalan Quetzal (GTQ)
        */
        GTQ,

        /**
        * Guyanese Dollar (GYD)
        */
        GYD,

        /**
        * Hong Kong Dollars (HKD)
        */
        HKD,

        /**
        * Honduran Lempira (HNL)
        */
        HNL,

        /**
        * Croatian Kuna (HRK)
        */
        HRK,

        /**
        * Haitian Gourde (HTG)
        */
        HTG,

        /**
        * Hungarian Forint (HUF)
        */
        HUF,

        /**
        * Indonesian Rupiah (IDR)
        */
        IDR,

        /**
        * Israeli New Shekel (NIS)
        */
        ILS,

        /**
        * Indian Rupees (INR)
        */
        INR,

        /**
        * Iraqi Dinar (IQD)
        */
        IQD,

        /**
        * Icelandic Kronur (ISK)
        */
        ISK,

        /**
        * Jersey Pound
        */
        JEP,

        /**
        * Jamaican Dollars (JMD)
        */
        JMD,

        /**
        * Jordanian Dinar (JOD)
        */
        JOD,

        /**
        * Japanese Yen (JPY)
        */
        JPY,

        /**
        * Kenyan Shilling (KES)
        */
        KES,

        /**
        * Kyrgyzstani Som (KGS)
        */
        KGS,

        /**
        * Cambodian Riel
        */
        KHR,

        /**
        * Comorian Franc (KMF)
        */
        KMF,

        /**
        * South Korean Won (KRW)
        */
        KRW,

        /**
        * Kuwaiti Dinar (KWD)
        */
        KWD,

        /**
        * Cayman Dollars (KYD)
        */
        KYD,

        /**
        * Kazakhstani Tenge (KZT)
        */
        KZT,

        /**
        * Laotian Kip (LAK)
        */
        LAK,

        /**
        * Lebanese Pounds (LBP)
        */
        LBP,

        /**
        * Sri Lankan Rupees (LKR)
        */
        LKR,

        /**
        * Liberian Dollar (LRD)
        */
        LRD,

        /**
        * Lesotho Loti (LSL)
        */
        LSL,

        /**
        * Lithuanian Litai (LTL)
        */
        LTL,

        /**
        * Latvian Lati (LVL)
        */
        LVL,

        /**
        * Moroccan Dirham
        */
        MAD,

        /**
        * Moldovan Leu (MDL)
        */
        MDL,

        /**
        * Malagasy Ariary (MGA)
        */
        MGA,

        /**
        * Macedonia Denar (MKD)
        */
        MKD,

        /**
        * Burmese Kyat (MMK)
        */
        MMK,

        /**
        * Mongolian Tugrik
        */
        MNT,

        /**
        * Macanese Pataca (MOP)
        */
        MOP,

        /**
        * Mauritian Rupee (MUR)
        */
        MUR,

        /**
        * Maldivian Rufiyaa (MVR)
        */
        MVR,

        /**
        * Malawian Kwacha (MWK)
        */
        MWK,

        /**
        * Mexican Pesos (MXN)
        */
        MXN,

        /**
        * Malaysian Ringgits (MYR)
        */
        MYR,

        /**
        * Mozambican Metical
        */
        MZN,

        /**
        * Namibian Dollar
        */
        NAD,

        /**
        * Nigerian Naira (NGN)
        */
        NGN,

        /**
        * Nicaraguan Córdoba (NIO)
        */
        NIO,

        /**
        * Norwegian Kroner (NOK)
        */
        NOK,

        /**
        * Nepalese Rupee (NPR)
        */
        NPR,

        /**
        * New Zealand Dollars (NZD)
        */
        NZD,

        /**
        * Omani Rial (OMR)
        */
        OMR,

        /**
        * Peruvian Nuevo Sol (PEN)
        */
        PEN,

        /**
        * Papua New Guinean Kina (PGK)
        */
        PGK,

        /**
        * Philippine Peso (PHP)
        */
        PHP,

        /**
        * Pakistani Rupee (PKR)
        */
        PKR,

        /**
        * Polish Zlotych (PLN)
        */
        PLN,

        /**
        * Paraguayan Guarani (PYG)
        */
        PYG,

        /**
        * Qatari Rial (QAR)
        */
        QAR,

        /**
        * Romanian Lei (RON)
        */
        RON,

        /**
        * Serbian dinar (RSD)
        */
        RSD,

        /**
        * Russian Rubles (RUB)
        */
        RUB,

        /**
        * Rwandan Franc (RWF)
        */
        RWF,

        /**
        * Saudi Riyal (SAR)
        */
        SAR,

        /**
        * Solomon Islands Dollar (SBD)
        */
        SBD,

        /**
        * Seychellois Rupee (SCR)
        */
        SCR,

        /**
        * Sudanese Pound (SDG)
        */
        SDG,

        /**
        * Swedish Kronor (SEK)
        */
        SEK,

        /**
        * Singapore Dollars (SGD)
        */
        SGD,

        /**
        * Surinamese Dollar (SRD)
        */
        SRD,

        /**
        * South Sudanese Pound (SSP)
        */
        SSP,

        /**
        * Sao Tome And Principe Dobra (STD)
        */
        STD,

        /**
        * Syrian Pound (SYP)
        */
        SYP,

        /**
        * Swazi Lilangeni (SZL)
        */
        SZL,

        /**
        * Thai baht (THB)
        */
        THB,

        /**
        * Turkmenistani Manat (TMT)
        */
        TMT,

        /**
        * Tunisian Dinar (TND)
        */
        TND,

        /**
        * Turkish Lira (TRY)
        */
        TRY,

        /**
        * Trinidad and Tobago Dollars (TTD)
        */
        TTD,

        /**
        * Taiwan Dollars (TWD)
        */
        TWD,

        /**
        * Tanzanian Shilling (TZS)
        */
        TZS,

        /**
        * Ukrainian Hryvnia (UAH)
        */
        UAH,

        /**
        * Ugandan Shilling (UGX)
        */
        UGX,

        /**
        * United States Dollars (USD)
        */
        USD,

        /**
        * Uruguayan Pesos (UYU)
        */
        UYU,

        /**
        * Uzbekistan som (UZS)
        */
        UZS,

        /**
        * Venezuelan Bolivares (VEF)
        */
        VEF,

        /**
        * Vietnamese đồng (VND)
        */
        VND,

        /**
        * Vanuatu Vatu (VUV)
        */
        VUV,

        /**
        * Samoan Tala (WST)
        */
        WST,

        /**
        * Central African CFA Franc (XAF)
        */
        XAF,

        /**
        * East Caribbean Dollar (XCD)
        */
        XCD,

        /**
        * West African CFA franc (XOF)
        */
        XOF,

        /**
        * CFP Franc (XPF)
        */
        XPF,

        /**
        * Yemeni Rial (YER)
        */
        YER,

        /**
        * South African Rand (ZAR)
        */
        ZAR,

        /**
        * Zambian Kwacha (ZMW)
        */
        ZMW,

        UNKNOWN_VALUE;

        public static CurrencyCode fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AED": {
                    return AED;
                }

                case "AFN": {
                    return AFN;
                }

                case "ALL": {
                    return ALL;
                }

                case "AMD": {
                    return AMD;
                }

                case "ANG": {
                    return ANG;
                }

                case "AOA": {
                    return AOA;
                }

                case "ARS": {
                    return ARS;
                }

                case "AUD": {
                    return AUD;
                }

                case "AWG": {
                    return AWG;
                }

                case "AZN": {
                    return AZN;
                }

                case "BAM": {
                    return BAM;
                }

                case "BBD": {
                    return BBD;
                }

                case "BDT": {
                    return BDT;
                }

                case "BGN": {
                    return BGN;
                }

                case "BHD": {
                    return BHD;
                }

                case "BIF": {
                    return BIF;
                }

                case "BND": {
                    return BND;
                }

                case "BOB": {
                    return BOB;
                }

                case "BRL": {
                    return BRL;
                }

                case "BSD": {
                    return BSD;
                }

                case "BTN": {
                    return BTN;
                }

                case "BWP": {
                    return BWP;
                }

                case "BYR": {
                    return BYR;
                }

                case "BZD": {
                    return BZD;
                }

                case "CAD": {
                    return CAD;
                }

                case "CDF": {
                    return CDF;
                }

                case "CHF": {
                    return CHF;
                }

                case "CLP": {
                    return CLP;
                }

                case "CNY": {
                    return CNY;
                }

                case "COP": {
                    return COP;
                }

                case "CRC": {
                    return CRC;
                }

                case "CVE": {
                    return CVE;
                }

                case "CZK": {
                    return CZK;
                }

                case "DKK": {
                    return DKK;
                }

                case "DOP": {
                    return DOP;
                }

                case "DZD": {
                    return DZD;
                }

                case "EGP": {
                    return EGP;
                }

                case "ETB": {
                    return ETB;
                }

                case "EUR": {
                    return EUR;
                }

                case "FJD": {
                    return FJD;
                }

                case "GBP": {
                    return GBP;
                }

                case "GEL": {
                    return GEL;
                }

                case "GHS": {
                    return GHS;
                }

                case "GMD": {
                    return GMD;
                }

                case "GTQ": {
                    return GTQ;
                }

                case "GYD": {
                    return GYD;
                }

                case "HKD": {
                    return HKD;
                }

                case "HNL": {
                    return HNL;
                }

                case "HRK": {
                    return HRK;
                }

                case "HTG": {
                    return HTG;
                }

                case "HUF": {
                    return HUF;
                }

                case "IDR": {
                    return IDR;
                }

                case "ILS": {
                    return ILS;
                }

                case "INR": {
                    return INR;
                }

                case "IQD": {
                    return IQD;
                }

                case "ISK": {
                    return ISK;
                }

                case "JEP": {
                    return JEP;
                }

                case "JMD": {
                    return JMD;
                }

                case "JOD": {
                    return JOD;
                }

                case "JPY": {
                    return JPY;
                }

                case "KES": {
                    return KES;
                }

                case "KGS": {
                    return KGS;
                }

                case "KHR": {
                    return KHR;
                }

                case "KMF": {
                    return KMF;
                }

                case "KRW": {
                    return KRW;
                }

                case "KWD": {
                    return KWD;
                }

                case "KYD": {
                    return KYD;
                }

                case "KZT": {
                    return KZT;
                }

                case "LAK": {
                    return LAK;
                }

                case "LBP": {
                    return LBP;
                }

                case "LKR": {
                    return LKR;
                }

                case "LRD": {
                    return LRD;
                }

                case "LSL": {
                    return LSL;
                }

                case "LTL": {
                    return LTL;
                }

                case "LVL": {
                    return LVL;
                }

                case "MAD": {
                    return MAD;
                }

                case "MDL": {
                    return MDL;
                }

                case "MGA": {
                    return MGA;
                }

                case "MKD": {
                    return MKD;
                }

                case "MMK": {
                    return MMK;
                }

                case "MNT": {
                    return MNT;
                }

                case "MOP": {
                    return MOP;
                }

                case "MUR": {
                    return MUR;
                }

                case "MVR": {
                    return MVR;
                }

                case "MWK": {
                    return MWK;
                }

                case "MXN": {
                    return MXN;
                }

                case "MYR": {
                    return MYR;
                }

                case "MZN": {
                    return MZN;
                }

                case "NAD": {
                    return NAD;
                }

                case "NGN": {
                    return NGN;
                }

                case "NIO": {
                    return NIO;
                }

                case "NOK": {
                    return NOK;
                }

                case "NPR": {
                    return NPR;
                }

                case "NZD": {
                    return NZD;
                }

                case "OMR": {
                    return OMR;
                }

                case "PEN": {
                    return PEN;
                }

                case "PGK": {
                    return PGK;
                }

                case "PHP": {
                    return PHP;
                }

                case "PKR": {
                    return PKR;
                }

                case "PLN": {
                    return PLN;
                }

                case "PYG": {
                    return PYG;
                }

                case "QAR": {
                    return QAR;
                }

                case "RON": {
                    return RON;
                }

                case "RSD": {
                    return RSD;
                }

                case "RUB": {
                    return RUB;
                }

                case "RWF": {
                    return RWF;
                }

                case "SAR": {
                    return SAR;
                }

                case "SBD": {
                    return SBD;
                }

                case "SCR": {
                    return SCR;
                }

                case "SDG": {
                    return SDG;
                }

                case "SEK": {
                    return SEK;
                }

                case "SGD": {
                    return SGD;
                }

                case "SRD": {
                    return SRD;
                }

                case "SSP": {
                    return SSP;
                }

                case "STD": {
                    return STD;
                }

                case "SYP": {
                    return SYP;
                }

                case "SZL": {
                    return SZL;
                }

                case "THB": {
                    return THB;
                }

                case "TMT": {
                    return TMT;
                }

                case "TND": {
                    return TND;
                }

                case "TRY": {
                    return TRY;
                }

                case "TTD": {
                    return TTD;
                }

                case "TWD": {
                    return TWD;
                }

                case "TZS": {
                    return TZS;
                }

                case "UAH": {
                    return UAH;
                }

                case "UGX": {
                    return UGX;
                }

                case "USD": {
                    return USD;
                }

                case "UYU": {
                    return UYU;
                }

                case "UZS": {
                    return UZS;
                }

                case "VEF": {
                    return VEF;
                }

                case "VND": {
                    return VND;
                }

                case "VUV": {
                    return VUV;
                }

                case "WST": {
                    return WST;
                }

                case "XAF": {
                    return XAF;
                }

                case "XCD": {
                    return XCD;
                }

                case "XOF": {
                    return XOF;
                }

                case "XPF": {
                    return XPF;
                }

                case "YER": {
                    return YER;
                }

                case "ZAR": {
                    return ZAR;
                }

                case "ZMW": {
                    return ZMW;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AED: {
                    return "AED";
                }

                case AFN: {
                    return "AFN";
                }

                case ALL: {
                    return "ALL";
                }

                case AMD: {
                    return "AMD";
                }

                case ANG: {
                    return "ANG";
                }

                case AOA: {
                    return "AOA";
                }

                case ARS: {
                    return "ARS";
                }

                case AUD: {
                    return "AUD";
                }

                case AWG: {
                    return "AWG";
                }

                case AZN: {
                    return "AZN";
                }

                case BAM: {
                    return "BAM";
                }

                case BBD: {
                    return "BBD";
                }

                case BDT: {
                    return "BDT";
                }

                case BGN: {
                    return "BGN";
                }

                case BHD: {
                    return "BHD";
                }

                case BIF: {
                    return "BIF";
                }

                case BND: {
                    return "BND";
                }

                case BOB: {
                    return "BOB";
                }

                case BRL: {
                    return "BRL";
                }

                case BSD: {
                    return "BSD";
                }

                case BTN: {
                    return "BTN";
                }

                case BWP: {
                    return "BWP";
                }

                case BYR: {
                    return "BYR";
                }

                case BZD: {
                    return "BZD";
                }

                case CAD: {
                    return "CAD";
                }

                case CDF: {
                    return "CDF";
                }

                case CHF: {
                    return "CHF";
                }

                case CLP: {
                    return "CLP";
                }

                case CNY: {
                    return "CNY";
                }

                case COP: {
                    return "COP";
                }

                case CRC: {
                    return "CRC";
                }

                case CVE: {
                    return "CVE";
                }

                case CZK: {
                    return "CZK";
                }

                case DKK: {
                    return "DKK";
                }

                case DOP: {
                    return "DOP";
                }

                case DZD: {
                    return "DZD";
                }

                case EGP: {
                    return "EGP";
                }

                case ETB: {
                    return "ETB";
                }

                case EUR: {
                    return "EUR";
                }

                case FJD: {
                    return "FJD";
                }

                case GBP: {
                    return "GBP";
                }

                case GEL: {
                    return "GEL";
                }

                case GHS: {
                    return "GHS";
                }

                case GMD: {
                    return "GMD";
                }

                case GTQ: {
                    return "GTQ";
                }

                case GYD: {
                    return "GYD";
                }

                case HKD: {
                    return "HKD";
                }

                case HNL: {
                    return "HNL";
                }

                case HRK: {
                    return "HRK";
                }

                case HTG: {
                    return "HTG";
                }

                case HUF: {
                    return "HUF";
                }

                case IDR: {
                    return "IDR";
                }

                case ILS: {
                    return "ILS";
                }

                case INR: {
                    return "INR";
                }

                case IQD: {
                    return "IQD";
                }

                case ISK: {
                    return "ISK";
                }

                case JEP: {
                    return "JEP";
                }

                case JMD: {
                    return "JMD";
                }

                case JOD: {
                    return "JOD";
                }

                case JPY: {
                    return "JPY";
                }

                case KES: {
                    return "KES";
                }

                case KGS: {
                    return "KGS";
                }

                case KHR: {
                    return "KHR";
                }

                case KMF: {
                    return "KMF";
                }

                case KRW: {
                    return "KRW";
                }

                case KWD: {
                    return "KWD";
                }

                case KYD: {
                    return "KYD";
                }

                case KZT: {
                    return "KZT";
                }

                case LAK: {
                    return "LAK";
                }

                case LBP: {
                    return "LBP";
                }

                case LKR: {
                    return "LKR";
                }

                case LRD: {
                    return "LRD";
                }

                case LSL: {
                    return "LSL";
                }

                case LTL: {
                    return "LTL";
                }

                case LVL: {
                    return "LVL";
                }

                case MAD: {
                    return "MAD";
                }

                case MDL: {
                    return "MDL";
                }

                case MGA: {
                    return "MGA";
                }

                case MKD: {
                    return "MKD";
                }

                case MMK: {
                    return "MMK";
                }

                case MNT: {
                    return "MNT";
                }

                case MOP: {
                    return "MOP";
                }

                case MUR: {
                    return "MUR";
                }

                case MVR: {
                    return "MVR";
                }

                case MWK: {
                    return "MWK";
                }

                case MXN: {
                    return "MXN";
                }

                case MYR: {
                    return "MYR";
                }

                case MZN: {
                    return "MZN";
                }

                case NAD: {
                    return "NAD";
                }

                case NGN: {
                    return "NGN";
                }

                case NIO: {
                    return "NIO";
                }

                case NOK: {
                    return "NOK";
                }

                case NPR: {
                    return "NPR";
                }

                case NZD: {
                    return "NZD";
                }

                case OMR: {
                    return "OMR";
                }

                case PEN: {
                    return "PEN";
                }

                case PGK: {
                    return "PGK";
                }

                case PHP: {
                    return "PHP";
                }

                case PKR: {
                    return "PKR";
                }

                case PLN: {
                    return "PLN";
                }

                case PYG: {
                    return "PYG";
                }

                case QAR: {
                    return "QAR";
                }

                case RON: {
                    return "RON";
                }

                case RSD: {
                    return "RSD";
                }

                case RUB: {
                    return "RUB";
                }

                case RWF: {
                    return "RWF";
                }

                case SAR: {
                    return "SAR";
                }

                case SBD: {
                    return "SBD";
                }

                case SCR: {
                    return "SCR";
                }

                case SDG: {
                    return "SDG";
                }

                case SEK: {
                    return "SEK";
                }

                case SGD: {
                    return "SGD";
                }

                case SRD: {
                    return "SRD";
                }

                case SSP: {
                    return "SSP";
                }

                case STD: {
                    return "STD";
                }

                case SYP: {
                    return "SYP";
                }

                case SZL: {
                    return "SZL";
                }

                case THB: {
                    return "THB";
                }

                case TMT: {
                    return "TMT";
                }

                case TND: {
                    return "TND";
                }

                case TRY: {
                    return "TRY";
                }

                case TTD: {
                    return "TTD";
                }

                case TWD: {
                    return "TWD";
                }

                case TZS: {
                    return "TZS";
                }

                case UAH: {
                    return "UAH";
                }

                case UGX: {
                    return "UGX";
                }

                case USD: {
                    return "USD";
                }

                case UYU: {
                    return "UYU";
                }

                case UZS: {
                    return "UZS";
                }

                case VEF: {
                    return "VEF";
                }

                case VND: {
                    return "VND";
                }

                case VUV: {
                    return "VUV";
                }

                case WST: {
                    return "WST";
                }

                case XAF: {
                    return "XAF";
                }

                case XCD: {
                    return "XCD";
                }

                case XOF: {
                    return "XOF";
                }

                case XPF: {
                    return "XPF";
                }

                case YER: {
                    return "YER";
                }

                case ZAR: {
                    return "ZAR";
                }

                case ZMW: {
                    return "ZMW";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CustomerQueryDefinition {
        void define(CustomerQuery _queryBuilder);
    }

    /**
    * A customer represents a customer account with the shop. Customer accounts store contact information
    * for the customer, saving logged-in customers the trouble of having to provide it at every checkout.
    */
    public static class CustomerQuery extends Query<CustomerQuery> {
        CustomerQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Indicates whether the customer has consented to be sent marketing material via email.
        */
        public CustomerQuery acceptsMarketing() {
            startField("acceptsMarketing");

            return this;
        }

        public class AddressesArguments extends Arguments {
            AddressesArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public AddressesArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public AddressesArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public AddressesArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public AddressesArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public AddressesArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface AddressesArgumentsDefinition {
            void define(AddressesArguments args);
        }

        /**
        * A list of addresses for the customer.
        */
        public CustomerQuery addresses(MailingAddressConnectionQueryDefinition queryDef) {
            return addresses(args -> {}, queryDef);
        }

        /**
        * A list of addresses for the customer.
        */
        public CustomerQuery addresses(AddressesArgumentsDefinition argsDef, MailingAddressConnectionQueryDefinition queryDef) {
            startField("addresses");

            AddressesArguments args = new AddressesArguments(_queryBuilder);
            argsDef.define(args);
            AddressesArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The date and time when the customer was created.
        */
        public CustomerQuery createdAt() {
            startField("createdAt");

            return this;
        }

        /**
        * The customer’s default address.
        */
        public CustomerQuery defaultAddress(MailingAddressQueryDefinition queryDef) {
            startField("defaultAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The customer’s name, email or phone number.
        */
        public CustomerQuery displayName() {
            startField("displayName");

            return this;
        }

        /**
        * The customer’s email address.
        */
        public CustomerQuery email() {
            startField("email");

            return this;
        }

        /**
        * The customer’s first name.
        */
        public CustomerQuery firstName() {
            startField("firstName");

            return this;
        }

        /**
        * A unique identifier for the customer.
        */
        public CustomerQuery id() {
            startField("id");

            return this;
        }

        /**
        * The customer’s last name.
        */
        public CustomerQuery lastName() {
            startField("lastName");

            return this;
        }

        public class OrdersArguments extends Arguments {
            OrdersArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public OrdersArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public OrdersArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public OrdersArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public OrdersArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public OrdersArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Sort the underlying list by the given key.
            */
            public OrdersArguments sortKey(OrderSortKeys value) {
                if (value != null) {
                    startArgument("sortKey");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Supported filter parameters:
            * - `processed_at`
            */
            public OrdersArguments query(String value) {
                if (value != null) {
                    startArgument("query");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }
        }

        public interface OrdersArgumentsDefinition {
            void define(OrdersArguments args);
        }

        /**
        * The orders associated with the customer.
        */
        public CustomerQuery orders(OrderConnectionQueryDefinition queryDef) {
            return orders(args -> {}, queryDef);
        }

        /**
        * The orders associated with the customer.
        */
        public CustomerQuery orders(OrdersArgumentsDefinition argsDef, OrderConnectionQueryDefinition queryDef) {
            startField("orders");

            OrdersArguments args = new OrdersArguments(_queryBuilder);
            argsDef.define(args);
            OrdersArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new OrderConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The customer’s phone number.
        */
        public CustomerQuery phone() {
            startField("phone");

            return this;
        }

        /**
        * The date and time when the customer information was updated.
        */
        public CustomerQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    /**
    * A customer represents a customer account with the shop. Customer accounts store contact information
    * for the customer, saving logged-in customers the trouble of having to provide it at every checkout.
    */
    public static class Customer extends AbstractResponse<Customer> {
        public Customer() {
        }

        public Customer(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "acceptsMarketing": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "addresses": {
                        responseData.put(key, new MailingAddressConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "defaultAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "displayName": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "email": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lastName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "orders": {
                        responseData.put(key, new OrderConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "phone": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Customer";
        }

        /**
        * Indicates whether the customer has consented to be sent marketing material via email.
        */

        public Boolean getAcceptsMarketing() {
            return (Boolean) get("acceptsMarketing");
        }

        public Customer setAcceptsMarketing(Boolean arg) {
            optimisticData.put(getKey("acceptsMarketing"), arg);
            return this;
        }

        /**
        * A list of addresses for the customer.
        */

        public MailingAddressConnection getAddresses() {
            return (MailingAddressConnection) get("addresses");
        }

        public Customer setAddresses(MailingAddressConnection arg) {
            optimisticData.put(getKey("addresses"), arg);
            return this;
        }

        /**
        * The date and time when the customer was created.
        */

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Customer setCreatedAt(DateTime arg) {
            optimisticData.put(getKey("createdAt"), arg);
            return this;
        }

        /**
        * The customer’s default address.
        */

        public MailingAddress getDefaultAddress() {
            return (MailingAddress) get("defaultAddress");
        }

        public Customer setDefaultAddress(MailingAddress arg) {
            optimisticData.put(getKey("defaultAddress"), arg);
            return this;
        }

        /**
        * The customer’s name, email or phone number.
        */

        public String getDisplayName() {
            return (String) get("displayName");
        }

        public Customer setDisplayName(String arg) {
            optimisticData.put(getKey("displayName"), arg);
            return this;
        }

        /**
        * The customer’s email address.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public Customer setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        /**
        * The customer’s first name.
        */

        public String getFirstName() {
            return (String) get("firstName");
        }

        public Customer setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        /**
        * A unique identifier for the customer.
        */

        public ID getId() {
            return (ID) get("id");
        }

        public Customer setId(ID arg) {
            optimisticData.put(getKey("id"), arg);
            return this;
        }

        /**
        * The customer’s last name.
        */

        public String getLastName() {
            return (String) get("lastName");
        }

        public Customer setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        /**
        * The orders associated with the customer.
        */

        public OrderConnection getOrders() {
            return (OrderConnection) get("orders");
        }

        public Customer setOrders(OrderConnection arg) {
            optimisticData.put(getKey("orders"), arg);
            return this;
        }

        /**
        * The customer’s phone number.
        */

        public String getPhone() {
            return (String) get("phone");
        }

        public Customer setPhone(String arg) {
            optimisticData.put(getKey("phone"), arg);
            return this;
        }

        /**
        * The date and time when the customer information was updated.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Customer setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "acceptsMarketing": return false;

                case "addresses": return true;

                case "createdAt": return false;

                case "defaultAddress": return true;

                case "displayName": return false;

                case "email": return false;

                case "firstName": return false;

                case "id": return false;

                case "lastName": return false;

                case "orders": return true;

                case "phone": return false;

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenQueryDefinition {
        void define(CustomerAccessTokenQuery _queryBuilder);
    }

    /**
    * A CustomerAccessToken represents the unique token required to make modifications to the customer
    * object.
    */
    public static class CustomerAccessTokenQuery extends Query<CustomerAccessTokenQuery> {
        CustomerAccessTokenQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer’s access token.
        */
        public CustomerAccessTokenQuery accessToken() {
            startField("accessToken");

            return this;
        }

        /**
        * The date and time when the customer access token expires.
        */
        public CustomerAccessTokenQuery expiresAt() {
            startField("expiresAt");

            return this;
        }
    }

    /**
    * A CustomerAccessToken represents the unique token required to make modifications to the customer
    * object.
    */
    public static class CustomerAccessToken extends AbstractResponse<CustomerAccessToken> {
        public CustomerAccessToken() {
        }

        public CustomerAccessToken(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "accessToken": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "expiresAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessToken";
        }

        /**
        * The customer’s access token.
        */

        public String getAccessToken() {
            return (String) get("accessToken");
        }

        public CustomerAccessToken setAccessToken(String arg) {
            optimisticData.put(getKey("accessToken"), arg);
            return this;
        }

        /**
        * The date and time when the customer access token expires.
        */

        public DateTime getExpiresAt() {
            return (DateTime) get("expiresAt");
        }

        public CustomerAccessToken setExpiresAt(DateTime arg) {
            optimisticData.put(getKey("expiresAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "accessToken": return false;

                case "expiresAt": return false;

                default: return false;
            }
        }
    }

    public static class CustomerAccessTokenCreateInput implements Serializable {
        private String email;

        private String password;

        public CustomerAccessTokenCreateInput(String email, String password) {
            this.email = email;

            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public CustomerAccessTokenCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerAccessTokenCreateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAccessTokenCreatePayloadQueryDefinition {
        void define(CustomerAccessTokenCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenCreatePayloadQuery extends Query<CustomerAccessTokenCreatePayloadQuery> {
        CustomerAccessTokenCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The newly created customer access token object.
        */
        public CustomerAccessTokenCreatePayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAccessTokenCreatePayloadQuery customerUserErrors(CustomerUserErrorQueryDefinition queryDef) {
            startField("customerUserErrors");

            _queryBuilder.append('{');
            queryDef.define(new CustomerUserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        *
        * @deprecated Use `customerUserErrors` instead
        */
        @Deprecated
        public CustomerAccessTokenCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAccessTokenCreatePayload extends AbstractResponse<CustomerAccessTokenCreatePayload> {
        public CustomerAccessTokenCreatePayload() {
        }

        public CustomerAccessTokenCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAccessToken": {
                        CustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessToken(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerUserErrors": {
                        List<CustomerUserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new CustomerUserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenCreatePayload";
        }

        /**
        * The newly created customer access token object.
        */

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerAccessTokenCreatePayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put(getKey("customerAccessToken"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<CustomerUserError> getCustomerUserErrors() {
            return (List<CustomerUserError>) get("customerUserErrors");
        }

        public CustomerAccessTokenCreatePayload setCustomerUserErrors(List<CustomerUserError> arg) {
            optimisticData.put(getKey("customerUserErrors"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        *
        * @deprecated Use `customerUserErrors` instead
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAccessToken": return true;

                case "customerUserErrors": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenDeletePayloadQueryDefinition {
        void define(CustomerAccessTokenDeletePayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenDeletePayloadQuery extends Query<CustomerAccessTokenDeletePayloadQuery> {
        CustomerAccessTokenDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The destroyed access token.
        */
        public CustomerAccessTokenDeletePayloadQuery deletedAccessToken() {
            startField("deletedAccessToken");

            return this;
        }

        /**
        * ID of the destroyed customer access token.
        */
        public CustomerAccessTokenDeletePayloadQuery deletedCustomerAccessTokenId() {
            startField("deletedCustomerAccessTokenId");

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAccessTokenDeletePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAccessTokenDeletePayload extends AbstractResponse<CustomerAccessTokenDeletePayload> {
        public CustomerAccessTokenDeletePayload() {
        }

        public CustomerAccessTokenDeletePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "deletedAccessToken": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "deletedCustomerAccessTokenId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenDeletePayload";
        }

        /**
        * The destroyed access token.
        */

        public String getDeletedAccessToken() {
            return (String) get("deletedAccessToken");
        }

        public CustomerAccessTokenDeletePayload setDeletedAccessToken(String arg) {
            optimisticData.put(getKey("deletedAccessToken"), arg);
            return this;
        }

        /**
        * ID of the destroyed customer access token.
        */

        public String getDeletedCustomerAccessTokenId() {
            return (String) get("deletedCustomerAccessTokenId");
        }

        public CustomerAccessTokenDeletePayload setDeletedCustomerAccessTokenId(String arg) {
            optimisticData.put(getKey("deletedCustomerAccessTokenId"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "deletedAccessToken": return false;

                case "deletedCustomerAccessTokenId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenRenewPayloadQueryDefinition {
        void define(CustomerAccessTokenRenewPayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenRenewPayloadQuery extends Query<CustomerAccessTokenRenewPayloadQuery> {
        CustomerAccessTokenRenewPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The renewed customer access token object.
        */
        public CustomerAccessTokenRenewPayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAccessTokenRenewPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAccessTokenRenewPayload extends AbstractResponse<CustomerAccessTokenRenewPayload> {
        public CustomerAccessTokenRenewPayload() {
        }

        public CustomerAccessTokenRenewPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAccessToken": {
                        CustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessToken(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenRenewPayload";
        }

        /**
        * The renewed customer access token object.
        */

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerAccessTokenRenewPayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put(getKey("customerAccessToken"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenRenewPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerActivateInput implements Serializable {
        private String activationToken;

        private String password;

        public CustomerActivateInput(String activationToken, String password) {
            this.activationToken = activationToken;

            this.password = password;
        }

        public String getActivationToken() {
            return activationToken;
        }

        public CustomerActivateInput setActivationToken(String activationToken) {
            this.activationToken = activationToken;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerActivateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("activationToken:");
            Query.appendQuotedString(_queryBuilder, activationToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append('}');
        }
    }

    public interface CustomerActivatePayloadQueryDefinition {
        void define(CustomerActivatePayloadQuery _queryBuilder);
    }

    public static class CustomerActivatePayloadQuery extends Query<CustomerActivatePayloadQuery> {
        CustomerActivatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer object.
        */
        public CustomerActivatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A newly created customer access token object for the customer.
        */
        public CustomerActivatePayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerActivatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerActivatePayload extends AbstractResponse<CustomerActivatePayload> {
        public CustomerActivatePayload() {
        }

        public CustomerActivatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAccessToken": {
                        CustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessToken(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerActivatePayload";
        }

        /**
        * The customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerActivatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * A newly created customer access token object for the customer.
        */

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerActivatePayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put(getKey("customerAccessToken"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerActivatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAddressCreatePayloadQueryDefinition {
        void define(CustomerAddressCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressCreatePayloadQuery extends Query<CustomerAddressCreatePayloadQuery> {
        CustomerAddressCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The new customer address object.
        */
        public CustomerAddressCreatePayloadQuery customerAddress(MailingAddressQueryDefinition queryDef) {
            startField("customerAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAddressCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAddressCreatePayload extends AbstractResponse<CustomerAddressCreatePayload> {
        public CustomerAddressCreatePayload() {
        }

        public CustomerAddressCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressCreatePayload";
        }

        /**
        * The new customer address object.
        */

        public MailingAddress getCustomerAddress() {
            return (MailingAddress) get("customerAddress");
        }

        public CustomerAddressCreatePayload setCustomerAddress(MailingAddress arg) {
            optimisticData.put(getKey("customerAddress"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAddress": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAddressDeletePayloadQueryDefinition {
        void define(CustomerAddressDeletePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressDeletePayloadQuery extends Query<CustomerAddressDeletePayloadQuery> {
        CustomerAddressDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * ID of the deleted customer address.
        */
        public CustomerAddressDeletePayloadQuery deletedCustomerAddressId() {
            startField("deletedCustomerAddressId");

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAddressDeletePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAddressDeletePayload extends AbstractResponse<CustomerAddressDeletePayload> {
        public CustomerAddressDeletePayload() {
        }

        public CustomerAddressDeletePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "deletedCustomerAddressId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressDeletePayload";
        }

        /**
        * ID of the deleted customer address.
        */

        public String getDeletedCustomerAddressId() {
            return (String) get("deletedCustomerAddressId");
        }

        public CustomerAddressDeletePayload setDeletedCustomerAddressId(String arg) {
            optimisticData.put(getKey("deletedCustomerAddressId"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "deletedCustomerAddressId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAddressUpdatePayloadQueryDefinition {
        void define(CustomerAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressUpdatePayloadQuery extends Query<CustomerAddressUpdatePayloadQuery> {
        CustomerAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer’s updated mailing address.
        */
        public CustomerAddressUpdatePayloadQuery customerAddress(MailingAddressQueryDefinition queryDef) {
            startField("customerAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAddressUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAddressUpdatePayload extends AbstractResponse<CustomerAddressUpdatePayload> {
        public CustomerAddressUpdatePayload() {
        }

        public CustomerAddressUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressUpdatePayload";
        }

        /**
        * The customer’s updated mailing address.
        */

        public MailingAddress getCustomerAddress() {
            return (MailingAddress) get("customerAddress");
        }

        public CustomerAddressUpdatePayload setCustomerAddress(MailingAddress arg) {
            optimisticData.put(getKey("customerAddress"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAddress": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerCreateInput implements Serializable {
        private String email;

        private String password;

        private Input<String> firstName = Input.undefined();

        private Input<String> lastName = Input.undefined();

        private Input<String> phone = Input.undefined();

        private Input<Boolean> acceptsMarketing = Input.undefined();

        public CustomerCreateInput(String email, String password) {
            this.email = email;

            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public CustomerCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerCreateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getFirstName() {
            return firstName.getValue();
        }

        public Input<String> getFirstNameInput() {
            return firstName;
        }

        public CustomerCreateInput setFirstName(String firstName) {
            this.firstName = Input.optional(firstName);
            return this;
        }

        public CustomerCreateInput setFirstNameInput(Input<String> firstName) {
            if (firstName == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName.getValue();
        }

        public Input<String> getLastNameInput() {
            return lastName;
        }

        public CustomerCreateInput setLastName(String lastName) {
            this.lastName = Input.optional(lastName);
            return this;
        }

        public CustomerCreateInput setLastNameInput(Input<String> lastName) {
            if (lastName == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.lastName = lastName;
            return this;
        }

        public String getPhone() {
            return phone.getValue();
        }

        public Input<String> getPhoneInput() {
            return phone;
        }

        public CustomerCreateInput setPhone(String phone) {
            this.phone = Input.optional(phone);
            return this;
        }

        public CustomerCreateInput setPhoneInput(Input<String> phone) {
            if (phone == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.phone = phone;
            return this;
        }

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing.getValue();
        }

        public Input<Boolean> getAcceptsMarketingInput() {
            return acceptsMarketing;
        }

        public CustomerCreateInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = Input.optional(acceptsMarketing);
            return this;
        }

        public CustomerCreateInput setAcceptsMarketingInput(Input<Boolean> acceptsMarketing) {
            if (acceptsMarketing == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            if (this.firstName.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("firstName:");
                if (firstName.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, firstName.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.lastName.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lastName:");
                if (lastName.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, lastName.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.phone.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("phone:");
                if (phone.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, phone.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.acceptsMarketing.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("acceptsMarketing:");
                if (acceptsMarketing.getValue() != null) {
                    _queryBuilder.append(acceptsMarketing.getValue());
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerCreatePayloadQueryDefinition {
        void define(CustomerCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerCreatePayloadQuery extends Query<CustomerCreatePayloadQuery> {
        CustomerCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The created customer object.
        */
        public CustomerCreatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerCreatePayload extends AbstractResponse<CustomerCreatePayload> {
        public CustomerCreatePayload() {
        }

        public CustomerCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerCreatePayload";
        }

        /**
        * The created customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerCreatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerDefaultAddressUpdatePayloadQueryDefinition {
        void define(CustomerDefaultAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CustomerDefaultAddressUpdatePayloadQuery extends Query<CustomerDefaultAddressUpdatePayloadQuery> {
        CustomerDefaultAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated customer object.
        */
        public CustomerDefaultAddressUpdatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerDefaultAddressUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerDefaultAddressUpdatePayload extends AbstractResponse<CustomerDefaultAddressUpdatePayload> {
        public CustomerDefaultAddressUpdatePayload() {
        }

        public CustomerDefaultAddressUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerDefaultAddressUpdatePayload";
        }

        /**
        * The updated customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerDefaultAddressUpdatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerDefaultAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    /**
    * Possible error codes that could be returned by a customer mutation.
    */
    public enum CustomerErrorCode {
        /**
        * Customer is unidentified.
        */
        UNIDENTIFIED_CUSTOMER,

        UNKNOWN_VALUE;

        public static CustomerErrorCode fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "UNIDENTIFIED_CUSTOMER": {
                    return UNIDENTIFIED_CUSTOMER;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case UNIDENTIFIED_CUSTOMER: {
                    return "UNIDENTIFIED_CUSTOMER";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CustomerRecoverPayloadQueryDefinition {
        void define(CustomerRecoverPayloadQuery _queryBuilder);
    }

    public static class CustomerRecoverPayloadQuery extends Query<CustomerRecoverPayloadQuery> {
        CustomerRecoverPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerRecoverPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerRecoverPayload extends AbstractResponse<CustomerRecoverPayload> {
        public CustomerRecoverPayload() {
        }

        public CustomerRecoverPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerRecoverPayload";
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerRecoverPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerResetInput implements Serializable {
        private String resetToken;

        private String password;

        public CustomerResetInput(String resetToken, String password) {
            this.resetToken = resetToken;

            this.password = password;
        }

        public String getResetToken() {
            return resetToken;
        }

        public CustomerResetInput setResetToken(String resetToken) {
            this.resetToken = resetToken;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerResetInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("resetToken:");
            Query.appendQuotedString(_queryBuilder, resetToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append('}');
        }
    }

    public interface CustomerResetPayloadQueryDefinition {
        void define(CustomerResetPayloadQuery _queryBuilder);
    }

    public static class CustomerResetPayloadQuery extends Query<CustomerResetPayloadQuery> {
        CustomerResetPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer object which was reset.
        */
        public CustomerResetPayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerResetPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerResetPayload extends AbstractResponse<CustomerResetPayload> {
        public CustomerResetPayload() {
        }

        public CustomerResetPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerResetPayload";
        }

        /**
        * The customer object which was reset.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerResetPayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerResetPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerUpdateInput implements Serializable {
        private Input<String> firstName = Input.undefined();

        private Input<String> lastName = Input.undefined();

        private Input<String> email = Input.undefined();

        private Input<String> phone = Input.undefined();

        private Input<String> password = Input.undefined();

        private Input<Boolean> acceptsMarketing = Input.undefined();

        public String getFirstName() {
            return firstName.getValue();
        }

        public Input<String> getFirstNameInput() {
            return firstName;
        }

        public CustomerUpdateInput setFirstName(String firstName) {
            this.firstName = Input.optional(firstName);
            return this;
        }

        public CustomerUpdateInput setFirstNameInput(Input<String> firstName) {
            if (firstName == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName.getValue();
        }

        public Input<String> getLastNameInput() {
            return lastName;
        }

        public CustomerUpdateInput setLastName(String lastName) {
            this.lastName = Input.optional(lastName);
            return this;
        }

        public CustomerUpdateInput setLastNameInput(Input<String> lastName) {
            if (lastName == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.lastName = lastName;
            return this;
        }

        public String getEmail() {
            return email.getValue();
        }

        public Input<String> getEmailInput() {
            return email;
        }

        public CustomerUpdateInput setEmail(String email) {
            this.email = Input.optional(email);
            return this;
        }

        public CustomerUpdateInput setEmailInput(Input<String> email) {
            if (email == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.email = email;
            return this;
        }

        public String getPhone() {
            return phone.getValue();
        }

        public Input<String> getPhoneInput() {
            return phone;
        }

        public CustomerUpdateInput setPhone(String phone) {
            this.phone = Input.optional(phone);
            return this;
        }

        public CustomerUpdateInput setPhoneInput(Input<String> phone) {
            if (phone == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.phone = phone;
            return this;
        }

        public String getPassword() {
            return password.getValue();
        }

        public Input<String> getPasswordInput() {
            return password;
        }

        public CustomerUpdateInput setPassword(String password) {
            this.password = Input.optional(password);
            return this;
        }

        public CustomerUpdateInput setPasswordInput(Input<String> password) {
            if (password == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.password = password;
            return this;
        }

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing.getValue();
        }

        public Input<Boolean> getAcceptsMarketingInput() {
            return acceptsMarketing;
        }

        public CustomerUpdateInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = Input.optional(acceptsMarketing);
            return this;
        }

        public CustomerUpdateInput setAcceptsMarketingInput(Input<Boolean> acceptsMarketing) {
            if (acceptsMarketing == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (this.firstName.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("firstName:");
                if (firstName.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, firstName.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.lastName.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lastName:");
                if (lastName.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, lastName.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.email.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                if (email.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, email.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.phone.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("phone:");
                if (phone.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, phone.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.password.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("password:");
                if (password.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, password.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.acceptsMarketing.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("acceptsMarketing:");
                if (acceptsMarketing.getValue() != null) {
                    _queryBuilder.append(acceptsMarketing.getValue());
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerUpdatePayloadQueryDefinition {
        void define(CustomerUpdatePayloadQuery _queryBuilder);
    }

    public static class CustomerUpdatePayloadQuery extends Query<CustomerUpdatePayloadQuery> {
        CustomerUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated customer object.
        */
        public CustomerUpdatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The newly created customer access token. If the customer's password is updated, all previous access
        * tokens
        * (including the one used to perform this mutation) become invalid, and a new token is generated.
        */
        public CustomerUpdatePayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerUpdatePayload extends AbstractResponse<CustomerUpdatePayload> {
        public CustomerUpdatePayload() {
        }

        public CustomerUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAccessToken": {
                        CustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessToken(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerUpdatePayload";
        }

        /**
        * The updated customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerUpdatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * The newly created customer access token. If the customer's password is updated, all previous access
        * tokens
        * (including the one used to perform this mutation) become invalid, and a new token is generated.
        */

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerUpdatePayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put(getKey("customerAccessToken"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerUserErrorQueryDefinition {
        void define(CustomerUserErrorQuery _queryBuilder);
    }

    /**
    * Represents an error that happens during execution of a customer mutation.
    */
    public static class CustomerUserErrorQuery extends Query<CustomerUserErrorQuery> {
        CustomerUserErrorQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Error code to uniquely identify the error.
        */
        public CustomerUserErrorQuery code() {
            startField("code");

            return this;
        }

        /**
        * Path to the input field which caused the error.
        */
        public CustomerUserErrorQuery field() {
            startField("field");

            return this;
        }

        /**
        * The error message.
        */
        public CustomerUserErrorQuery message() {
            startField("message");

            return this;
        }
    }

    /**
    * Represents an error that happens during execution of a customer mutation.
    */
    public static class CustomerUserError extends AbstractResponse<CustomerUserError> implements DisplayableError {
        public CustomerUserError() {
        }

        public CustomerUserError(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "code": {
                        CustomerErrorCode optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = CustomerErrorCode.fromGraphQl(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "field": {
                        List<String> optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            List<String> list1 = new ArrayList<>();
                            for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                list1.add(jsonAsString(element1, key));
                            }

                            optional1 = list1;
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "message": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerUserError";
        }

        /**
        * Error code to uniquely identify the error.
        */

        public CustomerErrorCode getCode() {
            return (CustomerErrorCode) get("code");
        }

        public CustomerUserError setCode(CustomerErrorCode arg) {
            optimisticData.put(getKey("code"), arg);
            return this;
        }

        /**
        * Path to the input field which caused the error.
        */

        public List<String> getField() {
            return (List<String>) get("field");
        }

        public CustomerUserError setField(List<String> arg) {
            optimisticData.put(getKey("field"), arg);
            return this;
        }

        /**
        * The error message.
        */

        public String getMessage() {
            return (String) get("message");
        }

        public CustomerUserError setMessage(String arg) {
            optimisticData.put(getKey("message"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "code": return false;

                case "field": return false;

                case "message": return false;

                default: return false;
            }
        }
    }

    /**
    * Digital wallet, such as Apple Pay, which can be used for accelerated checkouts.
    */
    public enum DigitalWallet {
        /**
        * Android Pay
        */
        ANDROID_PAY,

        /**
        * Apple Pay
        */
        APPLE_PAY,

        /**
        * Google Pay
        */
        GOOGLE_PAY,

        /**
        * Shopify Pay
        */
        SHOPIFY_PAY,

        UNKNOWN_VALUE;

        public static DigitalWallet fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "ANDROID_PAY": {
                    return ANDROID_PAY;
                }

                case "APPLE_PAY": {
                    return APPLE_PAY;
                }

                case "GOOGLE_PAY": {
                    return GOOGLE_PAY;
                }

                case "SHOPIFY_PAY": {
                    return SHOPIFY_PAY;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case ANDROID_PAY: {
                    return "ANDROID_PAY";
                }

                case APPLE_PAY: {
                    return "APPLE_PAY";
                }

                case GOOGLE_PAY: {
                    return "GOOGLE_PAY";
                }

                case SHOPIFY_PAY: {
                    return "SHOPIFY_PAY";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface DisplayableErrorQueryDefinition {
        void define(DisplayableErrorQuery _queryBuilder);
    }

    /**
    * Represents an error in the input of a mutation.
    */
    public static class DisplayableErrorQuery extends Query<DisplayableErrorQuery> {
        DisplayableErrorQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("__typename");
        }

        /**
        * Path to the input field which caused the error.
        */
        public DisplayableErrorQuery field() {
            startField("field");

            return this;
        }

        /**
        * The error message.
        */
        public DisplayableErrorQuery message() {
            startField("message");

            return this;
        }

        public DisplayableErrorQuery onCustomerUserError(CustomerUserErrorQueryDefinition queryDef) {
            startInlineFragment("CustomerUserError");
            queryDef.define(new CustomerUserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public DisplayableErrorQuery onUserError(UserErrorQueryDefinition queryDef) {
            startInlineFragment("UserError");
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }
    }

    public interface DisplayableError {
        String getGraphQlTypeName();

        List<String> getField();

        String getMessage();
    }

    /**
    * Represents an error in the input of a mutation.
    */
    public static class UnknownDisplayableError extends AbstractResponse<UnknownDisplayableError> implements DisplayableError {
        public UnknownDisplayableError() {
        }

        public UnknownDisplayableError(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "field": {
                        List<String> optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            List<String> list1 = new ArrayList<>();
                            for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                list1.add(jsonAsString(element1, key));
                            }

                            optional1 = list1;
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "message": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public static DisplayableError create(JsonObject fields) throws SchemaViolationError {
            String typeName = fields.getAsJsonPrimitive("__typename").getAsString();
            switch (typeName) {
                case "CustomerUserError": {
                    return new CustomerUserError(fields);
                }

                case "UserError": {
                    return new UserError(fields);
                }

                default: {
                    return new UnknownDisplayableError(fields);
                }
            }
        }

        public String getGraphQlTypeName() {
            return (String) get("__typename");
        }

        /**
        * Path to the input field which caused the error.
        */

        public List<String> getField() {
            return (List<String>) get("field");
        }

        public UnknownDisplayableError setField(List<String> arg) {
            optimisticData.put(getKey("field"), arg);
            return this;
        }

        /**
        * The error message.
        */

        public String getMessage() {
            return (String) get("message");
        }

        public UnknownDisplayableError setMessage(String arg) {
            optimisticData.put(getKey("message"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "field": return false;

                case "message": return false;

                default: return false;
            }
        }
    }

    public interface DomainQueryDefinition {
        void define(DomainQuery _queryBuilder);
    }

    /**
    * Represents a web address.
    */
    public static class DomainQuery extends Query<DomainQuery> {
        DomainQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The host name of the domain (eg: `example.com`).
        */
        public DomainQuery host() {
            startField("host");

            return this;
        }

        /**
        * Whether SSL is enabled or not.
        */
        public DomainQuery sslEnabled() {
            startField("sslEnabled");

            return this;
        }

        /**
        * The URL of the domain (eg: `https://example.com`).
        */
        public DomainQuery url() {
            startField("url");

            return this;
        }
    }

    /**
    * Represents a web address.
    */
    public static class Domain extends AbstractResponse<Domain> {
        public Domain() {
        }

        public Domain(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "host": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "sslEnabled": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "url": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Domain";
        }

        /**
        * The host name of the domain (eg: `example.com`).
        */

        public String getHost() {
            return (String) get("host");
        }

        public Domain setHost(String arg) {
            optimisticData.put(getKey("host"), arg);
            return this;
        }

        /**
        * Whether SSL is enabled or not.
        */

        public Boolean getSslEnabled() {
            return (Boolean) get("sslEnabled");
        }

        public Domain setSslEnabled(Boolean arg) {
            optimisticData.put(getKey("sslEnabled"), arg);
            return this;
        }

        /**
        * The URL of the domain (eg: `https://example.com`).
        */

        public String getUrl() {
            return (String) get("url");
        }

        public Domain setUrl(String arg) {
            optimisticData.put(getKey("url"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "host": return false;

                case "sslEnabled": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public interface FulfillmentQueryDefinition {
        void define(FulfillmentQuery _queryBuilder);
    }

    /**
    * Represents a single fulfillment in an order.
    */
    public static class FulfillmentQuery extends Query<FulfillmentQuery> {
        FulfillmentQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public class FulfillmentLineItemsArguments extends Arguments {
            FulfillmentLineItemsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Returns up to the first `n` elements from the list.
            */
            public FulfillmentLineItemsArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come after the specified cursor.
            */
            public FulfillmentLineItemsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Returns up to the last `n` elements from the list.
            */
            public FulfillmentLineItemsArguments last(Integer value) {
                if (value != null) {
                    startArgument("last");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Returns the elements that come before the specified cursor.
            */
            public FulfillmentLineItemsArguments before(String value) {
                if (value != null) {
                    startArgument("before");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            /**
            * Reverse the order of the underlying list.
            */
            public FulfillmentLineItemsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface FulfillmentLineItemsArgumentsDefinition {
            void define(FulfillmentLineItemsArguments args);
        }

        /**
        * List of the fulfillment's line items.
        */
        public FulfillmentQuery fulfillmentLineItems(FulfillmentLineItemConnectionQueryDefinition queryDef) {
            return fulfillmentLineItems(args -> {}, queryDef);
        }

        /**
        * List of the fulfillment's line items.
        */
        public FulfillmentQuery fulfillmentLineItems(FulfillmentLineItemsArgumentsDefinition argsDef, FulfillmentLineItemConnectionQueryDefinition queryDef) {
            startField("fulfillmentLineItems");

            FulfillmentLineItemsArguments args = new FulfillmentLineItemsArguments(_queryBuilder);
            argsDef.define(args);
            FulfillmentLineItemsArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new FulfillmentLineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The name of the tracking company.
        */
        public FulfillmentQuery trackingCompany() {
            startField("trackingCompany");

            return this;
        }

        public class TrackingInfoArguments extends Arguments {
            TrackingInfoArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncate the array result to this size.
            */
            public TrackingInfoArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface TrackingInfoArgumentsDefinition {
            void define(TrackingInfoArguments args);
        }

        /**
        * Tracking information associated with the fulfillment,
        * such as the tracking number and tracking URL.
        */
        public FulfillmentQuery trackingInfo(FulfillmentTrackingInfoQueryDefinition queryDef) {
            return trackingInfo(args -> {}, queryDef);
        }

        /**
        * Tracking information associated with the fulfillment,
        * such as the tracking number and tracking URL.
        */
        public FulfillmentQuery trackingInfo(TrackingInfoArgumentsDefinition argsDef, FulfillmentTrackingInfoQueryDefinition queryDef) {
            startField("trackingInfo");

            TrackingInfoArguments args = new TrackingInfoArguments(_queryBuilder);
            argsDef.define(args);
            TrackingInfoArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new FulfillmentTrackingInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * Represents a single fulfillment in an order.
    */
    public static class Fulfillment extends AbstractResponse<Fulfillment> {
        public Fulfillment() {
        }

        public Fulfillment(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "fulfillmentLineItems": {
                        responseData.put(key, new FulfillmentLineItemConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "trackingCompany": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "trackingInfo": {
                        List<FulfillmentTrackingInfo> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new FulfillmentTrackingInfo(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Fulfillment";
        }

        /**
        * List of the fulfillment's line items.
        */

        public FulfillmentLineItemConnection getFulfillmentLineItems() {
            return (FulfillmentLineItemConnection) get("fulfillmentLineItems");
        }

        public Fulfillment setFulfillmentLineItems(FulfillmentLineItemConnection arg) {
            optimisticData.put(getKey("fulfillmentLineItems"), arg);
            return this;
        }

        /**
        * The name of the tracking company.
        */

        public String getTrackingCompany() {
            return (String) get("trackingCompany");
        }

        public Fulfillment setTrackingCompany(String arg) {
            optimisticData.put(getKey("trackingCompany"), arg);
            return this;
        }

        /**
        * Tracking information associated with the fulfillment,
        * such as the tracking number and tracking URL.
        */

        public List<FulfillmentTrackingInfo> getTrackingInfo() {
            return (List<FulfillmentTrackingInfo>) get("trackingInfo");
        }

        public Fulfillment setTrackingInfo(List<FulfillmentTrackingInfo> arg) {
            optimisticData.put(getKey("trackingInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "fulfillmentLineItems": return true;

                case "trackingCompany": return false;

                case "trackingInfo": return true;

                default: return false;
            }
        }
    }

    public interface FulfillmentLineItemQueryDefinition {
        void define(FulfillmentLineItemQuery _queryBuilder);
    }

    /**
    * Represents a single line item in a fulfillment. There is at most one fulfillment line item for each
    * order line item.
    */
    public static class FulfillmentLineItemQuery extends Query<FulfillmentLineItemQuery> {
        FulfillmentLineItemQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The associated order's line item.
        */
        public FulfillmentLineItemQuery lineItem(OrderLineItemQueryDefinition queryDef) {
            startField("lineItem");

            _queryBuilder.append('{');
            queryDef.define(new OrderLineItemQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The amount fulfilled in this fulfillment.
        */
        public FulfillmentLineItemQuery quantity() {
            startField("quantity");

            return this;
        }
    }

    /**
    * Represents a single line item in a fulfillment. There is at most one fulfillment line item for each
    * order line item.
    */
    public static class FulfillmentLineItem extends AbstractResponse<FulfillmentLineItem> {
        public FulfillmentLineItem() {
        }

        public FulfillmentLineItem(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "lineItem": {
                        responseData.put(key, new OrderLineItem(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "quantity": {
                        responseData.put(key, jsonAsInteger(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "FulfillmentLineItem";
        }

        /**
        * The associated order's line item.
        */

        public OrderLineItem getLineItem() {
            return (OrderLineItem) get("lineItem");
        }

        public FulfillmentLineItem setLineItem(OrderLineItem arg) {
            optimisticData.put(getKey("lineItem"), arg);
            return this;
        }

        /**
        * The amount fulfilled in this fulfillment.
        */

        public Integer getQuantity() {
            return (Integer) get("quantity");
        }

        public FulfillmentLineItem setQuantity(Integer arg) {
            optimisticData.put(getKey("quantity"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "lineItem": return true;

                case "quantity": return false;

                default: return false;
            }
        }
    }

    public interface FulfillmentLineItemConnectionQueryDefinition {
        void define(FulfillmentLineItemConnectionQuery _queryBuilder);
    }

    public static class FulfillmentLineItemConnectionQuery extends Query<FulfillmentLineItemConnectionQuery> {
        FulfillmentLineItemConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public FulfillmentLineItemConnectionQuery edges(FulfillmentLineItemEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new FulfillmentLineItemEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public FulfillmentLineItemConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class FulfillmentLineItemConnection extends AbstractResponse<FulfillmentLineItemConnection> {
        public FulfillmentLineItemConnection() {
        }

        public FulfillmentLineItemConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<FulfillmentLineItemEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new FulfillmentLineItemEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "FulfillmentLineItemConnection";
        }

        /**
        * A list of edges.
        */

        public List<FulfillmentLineItemEdge> getEdges() {
            return (List<FulfillmentLineItemEdge>) get("edges");
        }

        public FulfillmentLineItemConnection setEdges(List<FulfillmentLineItemEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public FulfillmentLineItemConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface FulfillmentLineItemEdgeQueryDefinition {
        void define(FulfillmentLineItemEdgeQuery _queryBuilder);
    }

    public static class FulfillmentLineItemEdgeQuery extends Query<FulfillmentLineItemEdgeQuery> {
        FulfillmentLineItemEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public FulfillmentLineItemEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of FulfillmentLineItemEdge.
        */
        public FulfillmentLineItemEdgeQuery node(FulfillmentLineItemQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new FulfillmentLineItemQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class FulfillmentLineItemEdge extends AbstractResponse<FulfillmentLineItemEdge> {
        public FulfillmentLineItemEdge() {
        }

        public FulfillmentLineItemEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new FulfillmentLineItem(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "FulfillmentLineItemEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public FulfillmentLineItemEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of FulfillmentLineItemEdge.
        */

        public FulfillmentLineItem getNode() {
            return (FulfillmentLineItem) get("node");
        }

        public FulfillmentLineItemEdge setNode(FulfillmentLineItem arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface FulfillmentTrackingInfoQueryDefinition {
        void define(FulfillmentTrackingInfoQuery _queryBuilder);
    }

    /**
    * Tracking information associated with the fulfillment.
    */
    public static class FulfillmentTrackingInfoQuery extends Query<FulfillmentTrackingInfoQuery> {
        FulfillmentTrackingInfoQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The tracking number of the fulfillment.
        */
        public FulfillmentTrackingInfoQuery number() {
            startField("number");

            return this;
        }

        /**
        * The URL to track the fulfillment.
        */
        public FulfillmentTrackingInfoQuery url() {
            startField("url");

            return this;
        }
    }

    /**
    * Tracking information associated with the fulfillment.
    */
    public static class FulfillmentTrackingInfo extends AbstractResponse<FulfillmentTrackingInfo> {
        public FulfillmentTrackingInfo() {
        }

        public FulfillmentTrackingInfo(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "number": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "url": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "FulfillmentTrackingInfo";
        }

        /**
        * The tracking number of the fulfillment.
        */

        public String getNumber() {
            return (String) get("number");
        }

        public FulfillmentTrackingInfo setNumber(String arg) {
            optimisticData.put(getKey("number"), arg);
            return this;
        }

        /**
        * The URL to track the fulfillment.
        */

        public String getUrl() {
            return (String) get("url");
        }

        public FulfillmentTrackingInfo setUrl(String arg) {
            optimisticData.put(getKey("url"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "number": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public interface ImageQueryDefinition {
        void define(ImageQuery _queryBuilder);
    }

    /**
    * Represents an image resource.
    */
    public static class ImageQuery extends Query<ImageQuery> {
        ImageQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A word or phrase to share the nature or contents of an image.
        */
        public ImageQuery altText() {
            startField("altText");

            return this;
        }

        /**
        * A unique identifier for the image.
        */
        public ImageQuery id() {
            startField("id");

            return this;
        }

        /**
        * The location of the original (untransformed) image as a URL.
        */
        public ImageQuery originalSrc() {
            startField("originalSrc");

            return this;
        }

        /**
        * The location of the image as a URL.
        *
        * @deprecated Previously an image had a single `src` field. This could either return the original image
        location or a URL that contained transformations such as sizing or scale.

        These transformations were specified by arguments on the parent field.

        Now an image has two distinct URL fields: `originalSrc` and `transformedSrc`.

        * `originalSrc` - the original, untransformed image URL
        * `transformedSrc` - the image URL with transformations included

        To migrate to the new fields, image transformations should be moved from the parent field to `transformedSrc`.

        Before:
        ```graphql
        {
            shop {
                productImages(maxWidth: 200, scale: 2) {
                    edges {
                        node {
                            src
                        }
                    }
                }
            }
        }
        ```

        After:
        ```graphql
        {
            shop {
                productImages {
                    edges {
                        node {
                            transformedSrc(maxWidth: 200, scale: 2)
                        }
                    }
                }
            }
        }
        ```

        */
        @Deprecated
        public ImageQuery src() {
            startField("src");

            return this;
        }

        public class TransformedSrcArguments extends Arguments {
            TransformedSrcArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Image width in pixels between 1 and 5760.
            */
            public TransformedSrcArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image height in pixels between 1 and 5760.
            */
            public TransformedSrcArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Crops the image according to the specified region.
            */
            public TransformedSrcArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Image size multiplier for high-resolution retina displays. Must be between 1 and 3.
            */
            public TransformedSrcArguments scale(Integer value) {
                if (value != null) {
                    startArgument("scale");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Best effort conversion of image into content type (SVG -> PNG, Anything -> JGP, Anything -> WEBP are
            * supported).
            */
            public TransformedSrcArguments preferredContentType(ImageContentType value) {
                if (value != null) {
                    startArgument("preferredContentType");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }
        }

        public interface TransformedSrcArgumentsDefinition {
            void define(TransformedSrcArguments args);
        }

        /**
        * The location of the transformed image as a URL.
        * All transformation arguments are considered "best-effort". If they can be applied to an image, they
        * will be.
        * Otherwise any transformations which an image type does not support will be ignored.
        */
        public ImageQuery transformedSrc() {
            return transformedSrc(args -> {});
        }

        /**
        * The location of the transformed image as a URL.
        * All transformation arguments are considered "best-effort". If they can be applied to an image, they
        * will be.
        * Otherwise any transformations which an image type does not support will be ignored.
        */
        public ImageQuery transformedSrc(TransformedSrcArgumentsDefinition argsDef) {
            startField("transformedSrc");

            TransformedSrcArguments args = new TransformedSrcArguments(_queryBuilder);
            argsDef.define(args);
            TransformedSrcArguments.end(args);

            return this;
        }
    }

    /**
    * Represents an image resource.
    */
    public static class Image extends AbstractResponse<Image> {
        public Image() {
        }

        public Image(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "altText": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        ID optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ID(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "originalSrc": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "src": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "transformedSrc": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Image";
        }

        /**
        * A word or phrase to share the nature or contents of an image.
        */

        public String getAltText() {
            return (String) get("altText");
        }

        public Image setAltText(String arg) {
            optimisticData.put(getKey("altText"), arg);
            return this;
        }

        /**
        * A unique identifier for the image.
        */

        public ID getId() {
            return (ID) get("id");
        }

        public Image setId(ID arg) {
            optimisticData.put(getKey("id"), arg);
            return this;
        }

        /**
        * The location of the original (untransformed) image as a URL.
        */

        public String getOriginalSrc() {
            return (String) get("originalSrc");
        }

        public Image setOriginalSrc(String arg) {
            optimisticData.put(getKey("originalSrc"), arg);
            return this;
        }

        /**
        * The location of the image as a URL.
        *
        * @deprecated Previously an image had a single `src` field. This could either return the original image
        location or a URL that contained transformations such as sizing or scale.

        These transformations were specified by arguments on the parent field.

        Now an image has two distinct URL fields: `originalSrc` and `transformedSrc`.

        * `originalSrc` - the original, untransformed image URL
        * `transformedSrc` - the image URL with transformations included

        To migrate to the new fields, image transformations should be moved from the parent field to `transformedSrc`.

        Before:
        ```graphql
        {
            shop {
                productImages(maxWidth: 200, scale: 2) {
                    edges {
                        node {
                            src
                        }
                    }
                }
            }
        }
        ```

        After:
        ```graphql
        {
            shop {
                productImages {
                    edges {
                        node {
                            transformedSrc(maxWidth: 200, scale: 2)
                        }
                    }
                }
            }
        }
        ```

        */

        public String getSrc() {
            return (String) get("src");
        }

        public Image setSrc(String arg) {
            optimisticData.put(getKey("src"), arg);
            return this;
        }

        /**
        * The location of the transformed image as a URL.
        * All transformation arguments are considered "best-effort". If they can be applied to an image, they
        * will be.
        * Otherwise any transformations which an image type does not support will be ignored.
        */

        public String getTransformedSrc() {
            return (String) get("transformedSrc");
        }

        public Image setTransformedSrc(String arg) {
            optimisticData.put(getKey("transformedSrc"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "altText": return false;

                case "id": return false;

                case "originalSrc": return false;

                case "src": return false;

                case "transformedSrc": return false;

                default: return false;
            }
        }
    }

    public interface ImageConnectionQueryDefinition {
        void define(ImageConnectionQuery _queryBuilder);
    }

    public static class ImageConnectionQuery extends Query<ImageConnectionQuery> {
        ImageConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public ImageConnectionQuery edges(ImageEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ImageEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public ImageConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ImageConnection extends AbstractResponse<ImageConnection> {
        public ImageConnection() {
        }

        public ImageConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<ImageEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new ImageEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ImageConnection";
        }

        /**
        * A list of edges.
        */

        public List<ImageEdge> getEdges() {
            return (List<ImageEdge>) get("edges");
        }

        public ImageConnection setEdges(List<ImageEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ImageConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    /**
    * List of supported image content types.
    */
    public enum ImageContentType {
        JPG,

        PNG,

        WEBP,

        UNKNOWN_VALUE;

        public static ImageContentType fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "JPG": {
                    return JPG;
                }

                case "PNG": {
                    return PNG;
                }

                case "WEBP": {
                    return WEBP;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case JPG: {
                    return "JPG";
                }

                case PNG: {
                    return "PNG";
                }

                case WEBP: {
                    return "WEBP";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface ImageEdgeQueryDefinition {
        void define(ImageEdgeQuery _queryBuilder);
    }

    public static class ImageEdgeQuery extends Query<ImageEdgeQuery> {
        ImageEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public ImageEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of ImageEdge.
        */
        public ImageEdgeQuery node(ImageQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new ImageQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ImageEdge extends AbstractResponse<ImageEdge> {
        public ImageEdge() {
        }

        public ImageEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Image(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ImageEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public ImageEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of ImageEdge.
        */

        public Image getNode() {
            return (Image) get("node");
        }

        public ImageEdge setNode(Image arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface MailingAddressQueryDefinition {
        void define(MailingAddressQuery _queryBuilder);
    }

    /**
    * Represents a mailing address for customers and shipping.
    */
    public static class MailingAddressQuery extends Query<MailingAddressQuery> {
        MailingAddressQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The first line of the address. Typically the street address or PO Box number.
        */
        public MailingAddressQuery address1() {
            startField("address1");

            return this;
        }

        /**
        * The second line of the address. Typically the number of the apartment, suite, or unit.
        */
        public MailingAddressQuery address2() {
            startField("address2");

            return this;
        }

        /**
        * The name of the city, district, village, or town.
        */
        public MailingAddressQuery city() {
            startField("city");

            return this;
        }

        /**
        * The name of the customer's company or organization.
        */
        public MailingAddressQuery company() {
            startField("company");

            return this;
        }

        /**
        * The name of the country.
        */
        public MailingAddressQuery country() {
            startField("country");

            return this;
        }

        /**
        * The two-letter code for the country of the address.
        * For example, US.
        *
        * @deprecated Use `countryCodeV2` instead
        */
        @Deprecated
        public MailingAddressQuery countryCode() {
            startField("countryCode");

            return this;
        }

        /**
        * The two-letter code for the country of the address.
        * For example, US.
        */
        public MailingAddressQuery countryCodeV2() {
            startField("countryCodeV2");

            return this;
        }

        /**
        * The first name of the customer.
        */
        public MailingAddressQuery firstName() {
            startField("firstName");

            return this;
        }

        public class FormattedArguments extends Arguments {
            FormattedArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Whether to include the customer's name in the formatted address.
            */
            public FormattedArguments withName(Boolean value) {
                if (value != null) {
                    startArgument("withName");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Whether to include the customer's company in the formatted address.
            */
            public FormattedArguments withCompany(Boolean value) {
                if (value != null) {
                    startArgument("withCompany");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface FormattedArgumentsDefinition {
            void define(FormattedArguments args);
        }

        /**
        * A formatted version of the address, customized by the provided arguments.
        */
        public MailingAddressQuery formatted() {
            return formatted(args -> {});
        }

        /**
        * A formatted version of the address, customized by the provided arguments.
        */
        public MailingAddressQuery formatted(FormattedArgumentsDefinition argsDef) {
            startField("formatted");

            FormattedArguments args = new FormattedArguments(_queryBuilder);
            argsDef.define(args);
            FormattedArguments.end(args);

            return this;
        }

        /**
        * A comma-separated list of the values for city, province, and country.
        */
        public MailingAddressQuery formattedArea() {
            startField("formattedArea");

            return this;
        }

        /**
        * The last name of the customer.
        */
        public MailingAddressQuery lastName() {
            startField("lastName");

            return this;
        }

        /**
        * The latitude coordinate of the customer address.
        */
        public MailingAddressQuery latitude() {
            startField("latitude");

            return this;
        }

        /**
        * The longitude coordinate of the customer address.
        */
        public MailingAddressQuery longitude() {
            startField("longitude");

            return this;
        }

        /**
        * The full name of the customer, based on firstName and lastName.
        */
        public MailingAddressQuery name() {
            startField("name");

            return this;
        }

        /**
        * A unique phone number for the customer.
        * Formatted using E.164 standard. For example, _+16135551111_.
        */
        public MailingAddressQuery phone() {
            startField("phone");

            return this;
        }

        /**
        * The region of the address, such as the province, state, or district.
        */
        public MailingAddressQuery province() {
            startField("province");

            return this;
        }

        /**
        * The two-letter code for the region.
        * For example, ON.
        */
        public MailingAddressQuery provinceCode() {
            startField("provinceCode");

            return this;
        }

        /**
        * The zip or postal code of the address.
        */
        public MailingAddressQuery zip() {
            startField("zip");

            return this;
        }
    }

    /**
    * Represents a mailing address for customers and shipping.
    */
    public static class MailingAddress extends AbstractResponse<MailingAddress> implements Node {
        public MailingAddress() {
        }

        public MailingAddress(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "address1": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "address2": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "city": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "company": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "country": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "countryCode": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "countryCodeV2": {
                        CountryCode optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = CountryCode.fromGraphQl(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "formatted": {
                        List<String> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(jsonAsString(element1, key));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "formattedArea": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lastName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "latitude": {
                        Double optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsDouble(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "longitude": {
                        Double optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsDouble(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "name": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "phone": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "province": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "provinceCode": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "zip": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public MailingAddress(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "MailingAddress";
        }

        /**
        * The first line of the address. Typically the street address or PO Box number.
        */

        public String getAddress1() {
            return (String) get("address1");
        }

        public MailingAddress setAddress1(String arg) {
            optimisticData.put(getKey("address1"), arg);
            return this;
        }

        /**
        * The second line of the address. Typically the number of the apartment, suite, or unit.
        */

        public String getAddress2() {
            return (String) get("address2");
        }

        public MailingAddress setAddress2(String arg) {
            optimisticData.put(getKey("address2"), arg);
            return this;
        }

        /**
        * The name of the city, district, village, or town.
        */

        public String getCity() {
            return (String) get("city");
        }

        public MailingAddress setCity(String arg) {
            optimisticData.put(getKey("city"), arg);
            return this;
        }

        /**
        * The name of the customer's company or organization.
        */

        public String getCompany() {
            return (String) get("company");
        }

        public MailingAddress setCompany(String arg) {
            optimisticData.put(getKey("company"), arg);
            return this;
        }

        /**
        * The name of the country.
        */

        public String getCountry() {
            return (String) get("country");
        }

        public MailingAddress setCountry(String arg) {
            optimisticData.put(getKey("country"), arg);
            return this;
        }

        /**
        * The two-letter code for the country of the address.
        * For example, US.
        *
        * @deprecated Use `countryCodeV2` instead
        */

        public String getCountryCode() {
            return (String) get("countryCode");
        }

        public MailingAddress setCountryCode(String arg) {
            optimisticData.put(getKey("countryCode"), arg);
            return this;
        }

        /**
        * The two-letter code for the country of the address.
        * For example, US.
        */

        public CountryCode getCountryCodeV2() {
            return (CountryCode) get("countryCodeV2");
        }

        public MailingAddress setCountryCodeV2(CountryCode arg) {
            optimisticData.put(getKey("countryCodeV2"), arg);
            return this;
        }

        /**
        * The first name of the customer.
        */

        public String getFirstName() {
            return (String) get("firstName");
        }

        public MailingAddress setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        /**
        * A formatted version of the address, customized by the provided arguments.
        */

        public List<String> getFormatted() {
            return (List<String>) get("formatted");
        }

        public MailingAddress setFormatted(List<String> arg) {
            optimisticData.put(getKey("formatted"), arg);
            return this;
        }

        /**
        * A comma-separated list of the values for city, province, and country.
        */

        public String getFormattedArea() {
            return (String) get("formattedArea");
        }

        public MailingAddress setFormattedArea(String arg) {
            optimisticData.put(getKey("formattedArea"), arg);
            return this;
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The last name of the customer.
        */

        public String getLastName() {
            return (String) get("lastName");
        }

        public MailingAddress setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        /**
        * The latitude coordinate of the customer address.
        */

        public Double getLatitude() {
            return (Double) get("latitude");
        }

        public MailingAddress setLatitude(Double arg) {
            optimisticData.put(getKey("latitude"), arg);
            return this;
        }

        /**
        * The longitude coordinate of the customer address.
        */

        public Double getLongitude() {
            return (Double) get("longitude");
        }

        public MailingAddress setLongitude(Double arg) {
            optimisticData.put(getKey("longitude"), arg);
            return this;
        }

        /**
        * The full name of the customer, based on firstName and lastName.
        */

        public String getName() {
            return (String) get("name");
        }

        public MailingAddress setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        /**
        * A unique phone number for the customer.
        * Formatted using E.164 standard. For example, _+16135551111_.
        */

        public String getPhone() {
            return (String) get("phone");
        }

        public MailingAddress setPhone(String arg) {
            optimisticData.put(getKey("phone"), arg);
            return this;
        }

        /**
        * The region of the address, such as the province, state, or district.
        */

        public String getProvince() {
            return (String) get("province");
        }

        public MailingAddress setProvince(String arg) {
            optimisticData.put(getKey("province"), arg);
            return this;
        }

        /**
        * The two-letter code for the region.
        * For example, ON.
        */

        public String getProvinceCode() {
            return (String) get("provinceCode");
        }

        public MailingAddress setProvinceCode(String arg) {
            optimisticData.put(getKey("provinceCode"), arg);
            return this;
        }

        /**
        * The zip or postal code of the address.
        */

        public String getZip() {
            return (String) get("zip");
        }

        public MailingAddress setZip(String arg) {
            optimisticData.put(getKey("zip"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "address1": return false;

                case "address2": return false;

                case "city": return false;

                case "company": return false;

                case "country": return false;

                case "countryCode": return false;

                case "countryCodeV2": return false;

                case "firstName": return false;

                case "formatted": return false;

                case "formattedArea": return false;

                case "id": return false;

                case "lastName": return false;

                case "latitude": return false;

                case "longitude": return false;

                case "name": return false;

                case "phone": return false;

                case "province": return false;

                case "provinceCode": return false;

                case "zip": return false;

                default: return false;
            }
        }
    }

    public interface MailingAddressConnectionQueryDefinition {
        void define(MailingAddressConnectionQuery _queryBuilder);
    }

    public static class MailingAddressConnectionQuery extends Query<MailingAddressConnectionQuery> {
        MailingAddressConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public MailingAddressConnectionQuery edges(MailingAddressEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public MailingAddressConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class MailingAddressConnection extends AbstractResponse<MailingAddressConnection> {
        public MailingAddressConnection() {
        }

        public MailingAddressConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<MailingAddressEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new MailingAddressEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "MailingAddressConnection";
        }

        /**
        * A list of edges.
        */

        public List<MailingAddressEdge> getEdges() {
            return (List<MailingAddressEdge>) get("edges");
        }

        public MailingAddressConnection setEdges(List<MailingAddressEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public MailingAddressConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface MailingAddressEdgeQueryDefinition {
        void define(MailingAddressEdgeQuery _queryBuilder);
    }

    public static class MailingAddressEdgeQuery extends Query<MailingAddressEdgeQuery> {
        MailingAddressEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A cursor for use in pagination.
        */
        public MailingAddressEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        /**
        * The item at the end of MailingAddressEdge.
        */
        public MailingAddressEdgeQuery node(MailingAddressQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class MailingAddressEdge extends AbstractResponse<MailingAddressEdge> {
        public MailingAddressEdge() {
        }

        public MailingAddressEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new MailingAddress(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "MailingAddressEdge";
        }

        /**
        * A cursor for use in pagination.
        */

        public String getCursor() {
            return (String) get("cursor");
        }

        public MailingAddressEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        /**
        * The item at the end of MailingAddressEdge.
        */

        public MailingAddress getNode() {
            return (MailingAddress) get("node");
        }

        public MailingAddressEdge setNode(MailingAddress arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public static class MailingAddressInput implements Serializable {
        private Input<String> address1 = Input.undefined();

        private Input<String> address2 = Input.undefined();

        private Input<String> city = Input.undefined();

        private Input<String> company = Input.undefined();

        private Input<String> country = Input.undefined();

        private Input<String> firstName = Input.undefined();

        private Input<String> lastName = Input.undefined();

        private Input<String> phone = Input.undefined();

        private Input<String> province = Input.undefined();

        private Input<String> zip = Input.undefined();

        public String getAddress1() {
            return address1.getValue();
        }

        public Input<String> getAddress1Input() {
            return address1;
        }

        public MailingAddressInput setAddress1(String address1) {
            this.address1 = Input.optional(address1);
            return this;
        }

        public MailingAddressInput setAddress1Input(Input<String> address1) {
            if (address1 == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.address1 = address1;
            return this;
        }

        public String getAddress2() {
            return address2.getValue();
        }

        public Input<String> getAddress2Input() {
            return address2;
        }

        public MailingAddressInput setAddress2(String address2) {
            this.address2 = Input.optional(address2);
            return this;
        }

        public MailingAddressInput setAddress2Input(Input<String> address2) {
            if (address2 == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.address2 = address2;
            return this;
        }

        public String getCity() {
            return city.getValue();
        }

        public Input<String> getCityInput() {
            return city;
        }

        public MailingAddressInput setCity(String city) {
            this.city = Input.optional(city);
            return this;
        }

        public MailingAddressInput setCityInput(Input<String> city) {
            if (city == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.city = city;
            return this;
        }

        public String getCompany() {
            return company.getValue();
        }

        public Input<String> getCompanyInput() {
            return company;
        }

        public MailingAddressInput setCompany(String company) {
            this.company = Input.optional(company);
            return this;
        }

        public MailingAddressInput setCompanyInput(Input<String> company) {
            if (company == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.company = company;
            return this;
        }

        public String getCountry() {
            return country.getValue();
        }

        public Input<String> getCountryInput() {
            return country;
        }

        public MailingAddressInput setCountry(String country) {
            this.country = Input.optional(country);
            return this;
        }

        public MailingAddressInput setCountryInput(Input<String> country) {
            if (country == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.country = country;
            return this;
        }

        public String getFirstName() {
            return firstName.getValue();
        }

        public Input<String> getFirstNameInput() {
            return firstName;
        }

        public MailingAddressInput setFirstName(String firstName) {
            this.firstName = Input.optional(firstName);
            return this;
        }

        public MailingAddressInput setFirstNameInput(Input<String> firstName) {
            if (firstName == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName.getValue();
        }

        public Input<String> getLastNameInput() {
            return lastName;
        }

        public MailingAddressInput setLastName(String lastName) {
            this.lastName = Input.optional(lastName);
            return this;
        }

        public MailingAddressInput setLastNameInput(Input<String> lastName) {
            if (lastName == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.lastName = lastName;
            return this;
        }

        public String getPhone() {
            return phone.getValue();
        }

        public Input<String> getPhoneInput() {
            return phone;
        }

        public MailingAddressInput setPhone(String phone) {
            this.phone = Input.optional(phone);
            return this;
        }

        public MailingAddressInput setPhoneInput(Input<String> phone) {
            if (phone == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.phone = phone;
            return this;
        }

        public String getProvince() {
            return province.getValue();
        }

        public Input<String> getProvinceInput() {
            return province;
        }

        public MailingAddressInput setProvince(String province) {
            this.province = Input.optional(province);
            return this;
        }

        public MailingAddressInput setProvinceInput(Input<String> province) {
            if (province == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.province = province;
            return this;
        }

        public String getZip() {
            return zip.getValue();
        }

        public Input<String> getZipInput() {
            return zip;
        }

        public MailingAddressInput setZip(String zip) {
            this.zip = Input.optional(zip);
            return this;
        }

        public MailingAddressInput setZipInput(Input<String> zip) {
            if (zip == null) {
                throw new IllegalArgumentException("Input can not be null");
            }
            this.zip = zip;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (this.address1.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("address1:");
                if (address1.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, address1.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.address2.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("address2:");
                if (address2.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, address2.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.city.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("city:");
                if (city.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, city.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.company.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("company:");
                if (company.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, company.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.country.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("country:");
                if (country.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, country.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.firstName.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("firstName:");
                if (firstName.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, firstName.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.lastName.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lastName:");
                if (lastName.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, lastName.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.phone.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("phone:");
                if (phone.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, phone.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.province.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("province:");
                if (province.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, province.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            if (this.zip.isDefined()) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("zip:");
                if (zip.getValue() != null) {
                    Query.appendQuotedString(_queryBuilder, zip.getValue().toString());
                } else {
                    _queryBuilder.append("null");
                }
            }

            _queryBuilder.append('}');
        }
    }

    public interface MoneyV2QueryDefinition {
        void define(MoneyV2Query _queryBuilder);
    }

    /**
    * A monetary value with currency.
    * To format currencies, combine this type's amount and currencyCode fields with your client's locale.
    * For example, in JavaScript you could use Intl.NumberFormat:
    * ```js
    * new Intl.NumberFormat(locale, {
        * style: 'currency',
        * currency: currencyCode
        * }).format(amount);
        * ```
        * Other formatting libraries include:
        * * iOS - [NumberFormatter](https://developer.apple.com/documentation/foundation/numberformatter)
        * * Android - [NumberFormat](https://developer.android.com/reference/java/text/NumberFormat.html)
        * * PHP - [NumberFormatter](http://php.net/manual/en/class.numberformatter.php)
        * For a more general solution, the [Unicode CLDR number formatting database] is available with many
        * implementations
        * (such as [TwitterCldr](https://github.com/twitter/twitter-cldr-rb)).
        */
        public static class MoneyV2Query extends Query<MoneyV2Query> {
            MoneyV2Query(StringBuilder _queryBuilder) {
                super(_queryBuilder);
            }

            /**
            * Decimal money amount.
            */
            public MoneyV2Query amount() {
                startField("amount");

                return this;
            }

            /**
            * Currency of the money.
            */
            public MoneyV2Query currencyCode() {
                startField("currencyCode");

                return this;
            }
        }

        /**
        * A monetary value with currency.
        * To format currencies, combine this type's amount and currencyCode fields with your client's locale.
        * For example, in JavaScript you could use Intl.NumberFormat:
        * ```js
        * new Intl.NumberFormat(locale, {
            * style: 'currency',
            * currency: currencyCode
            * }).format(amount);
            * ```
            * Other formatting libraries include:
            * * iOS - [NumberFormatter](https://developer.apple.com/documentation/foundation/numberformatter)
            * * Android - [NumberFormat](https://developer.android.com/reference/java/text/NumberFormat.html)
            * * PHP - [NumberFormatter](http://php.net/manual/en/class.numberformatter.php)
            * For a more general solution, the [Unicode CLDR number formatting database] is available with many
            * implementations
            * (such as [TwitterCldr](https://github.com/twitter/twitter-cldr-rb)).
            */
            public static class MoneyV2 extends AbstractResponse<MoneyV2> {
                public MoneyV2() {
                }

                public MoneyV2(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "amount": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "currencyCode": {
                                responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "MoneyV2";
                }

                /**
                * Decimal money amount.
                */

                public String getAmount() {
                    return (String) get("amount");
                }

                public MoneyV2 setAmount(String arg) {
                    optimisticData.put(getKey("amount"), arg);
                    return this;
                }

                /**
                * Currency of the money.
                */

                public CurrencyCode getCurrencyCode() {
                    return (CurrencyCode) get("currencyCode");
                }

                public MoneyV2 setCurrencyCode(CurrencyCode arg) {
                    optimisticData.put(getKey("currencyCode"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "amount": return false;

                        case "currencyCode": return false;

                        default: return false;
                    }
                }
            }

            public interface MutationQueryDefinition {
                void define(MutationQuery _queryBuilder);
            }

            /**
            * The schema’s entry-point for mutations. This acts as the public, top-level API from which all
            * mutation queries must start.
            */
            public static class MutationQuery extends Query<MutationQuery> {
                MutationQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * Updates the attributes of a checkout.
                */
                public MutationQuery checkoutAttributesUpdate(ID checkoutId, CheckoutAttributesUpdateInput input, CheckoutAttributesUpdatePayloadQueryDefinition queryDef) {
                    startField("checkoutAttributesUpdate");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",input:");
                    input.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutAttributesUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public MutationQuery checkoutCompleteFree(ID checkoutId, CheckoutCompleteFreePayloadQueryDefinition queryDef) {
                    startField("checkoutCompleteFree");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutCompleteFreePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Completes a checkout using a credit card token from Shopify's Vault.
                */
                public MutationQuery checkoutCompleteWithCreditCard(ID checkoutId, CreditCardPaymentInput payment, CheckoutCompleteWithCreditCardPayloadQueryDefinition queryDef) {
                    startField("checkoutCompleteWithCreditCard");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",payment:");
                    payment.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutCompleteWithCreditCardPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Completes a checkout with a tokenized payment.
                */
                public MutationQuery checkoutCompleteWithTokenizedPayment(ID checkoutId, TokenizedPaymentInput payment, CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition queryDef) {
                    startField("checkoutCompleteWithTokenizedPayment");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",payment:");
                    payment.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutCompleteWithTokenizedPaymentPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Creates a new checkout.
                */
                public MutationQuery checkoutCreate(CheckoutCreateInput input, CheckoutCreatePayloadQueryDefinition queryDef) {
                    startField("checkoutCreate");

                    _queryBuilder.append("(input:");
                    input.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutCreatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Associates a customer to the checkout.
                */
                public MutationQuery checkoutCustomerAssociate(ID checkoutId, String customerAccessToken, CheckoutCustomerAssociatePayloadQueryDefinition queryDef) {
                    startField("checkoutCustomerAssociate");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutCustomerAssociatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Disassociates the current checkout customer from the checkout.
                */
                public MutationQuery checkoutCustomerDisassociate(ID checkoutId, CheckoutCustomerDisassociatePayloadQueryDefinition queryDef) {
                    startField("checkoutCustomerDisassociate");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutCustomerDisassociatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Applies a discount to an existing checkout using a discount code.
                */
                public MutationQuery checkoutDiscountCodeApply(String discountCode, ID checkoutId, CheckoutDiscountCodeApplyPayloadQueryDefinition queryDef) {
                    startField("checkoutDiscountCodeApply");

                    _queryBuilder.append("(discountCode:");
                    Query.appendQuotedString(_queryBuilder, discountCode.toString());

                    _queryBuilder.append(",checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutDiscountCodeApplyPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Removes the applied discount from an existing checkout.
                */
                public MutationQuery checkoutDiscountCodeRemove(ID checkoutId, CheckoutDiscountCodeRemovePayloadQueryDefinition queryDef) {
                    startField("checkoutDiscountCodeRemove");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutDiscountCodeRemovePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates the email on an existing checkout.
                */
                public MutationQuery checkoutEmailUpdate(ID checkoutId, String email, CheckoutEmailUpdatePayloadQueryDefinition queryDef) {
                    startField("checkoutEmailUpdate");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",email:");
                    Query.appendQuotedString(_queryBuilder, email.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutEmailUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Applies a gift card to an existing checkout using a gift card code. This will replace all currently
                * applied gift cards.
                *
                * @deprecated Use `checkoutGiftCardsAppend` instead
                */
                @Deprecated
                public MutationQuery checkoutGiftCardApply(String giftCardCode, ID checkoutId, CheckoutGiftCardApplyPayloadQueryDefinition queryDef) {
                    startField("checkoutGiftCardApply");

                    _queryBuilder.append("(giftCardCode:");
                    Query.appendQuotedString(_queryBuilder, giftCardCode.toString());

                    _queryBuilder.append(",checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutGiftCardApplyPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Removes an applied gift card from the checkout.
                */
                public MutationQuery checkoutGiftCardRemove(ID appliedGiftCardId, ID checkoutId, CheckoutGiftCardRemovePayloadQueryDefinition queryDef) {
                    startField("checkoutGiftCardRemove");

                    _queryBuilder.append("(appliedGiftCardId:");
                    Query.appendQuotedString(_queryBuilder, appliedGiftCardId.toString());

                    _queryBuilder.append(",checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutGiftCardRemovePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Appends gift cards to an existing checkout.
                */
                public MutationQuery checkoutGiftCardsAppend(List<String> giftCardCodes, ID checkoutId, CheckoutGiftCardsAppendPayloadQueryDefinition queryDef) {
                    startField("checkoutGiftCardsAppend");

                    _queryBuilder.append("(giftCardCodes:");
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (String item1 : giftCardCodes) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            Query.appendQuotedString(_queryBuilder, item1.toString());
                        }
                    }
                    _queryBuilder.append(']');

                    _queryBuilder.append(",checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutGiftCardsAppendPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Adds a list of line items to a checkout.
                */
                public MutationQuery checkoutLineItemsAdd(List<CheckoutLineItemInput> lineItems, ID checkoutId, CheckoutLineItemsAddPayloadQueryDefinition queryDef) {
                    startField("checkoutLineItemsAdd");

                    _queryBuilder.append("(lineItems:");
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (CheckoutLineItemInput item1 : lineItems) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');

                    _queryBuilder.append(",checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutLineItemsAddPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Removes line items from an existing checkout
                */
                public MutationQuery checkoutLineItemsRemove(ID checkoutId, List<ID> lineItemIds, CheckoutLineItemsRemovePayloadQueryDefinition queryDef) {
                    startField("checkoutLineItemsRemove");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",lineItemIds:");
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (ID item1 : lineItemIds) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            Query.appendQuotedString(_queryBuilder, item1.toString());
                        }
                    }
                    _queryBuilder.append(']');

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutLineItemsRemovePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates line items on a checkout.
                */
                public MutationQuery checkoutLineItemsUpdate(ID checkoutId, List<CheckoutLineItemUpdateInput> lineItems, CheckoutLineItemsUpdatePayloadQueryDefinition queryDef) {
                    startField("checkoutLineItemsUpdate");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",lineItems:");
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (CheckoutLineItemUpdateInput item1 : lineItems) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutLineItemsUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates the shipping address of an existing checkout.
                */
                public MutationQuery checkoutShippingAddressUpdate(MailingAddressInput shippingAddress, ID checkoutId, CheckoutShippingAddressUpdatePayloadQueryDefinition queryDef) {
                    startField("checkoutShippingAddressUpdate");

                    _queryBuilder.append("(shippingAddress:");
                    shippingAddress.appendTo(_queryBuilder);

                    _queryBuilder.append(",checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutShippingAddressUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates the shipping lines on an existing checkout.
                */
                public MutationQuery checkoutShippingLineUpdate(ID checkoutId, String shippingRateHandle, CheckoutShippingLineUpdatePayloadQueryDefinition queryDef) {
                    startField("checkoutShippingLineUpdate");

                    _queryBuilder.append("(checkoutId:");
                    Query.appendQuotedString(_queryBuilder, checkoutId.toString());

                    _queryBuilder.append(",shippingRateHandle:");
                    Query.appendQuotedString(_queryBuilder, shippingRateHandle.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutShippingLineUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Creates a customer access token.
                * The customer access token is required to modify the customer object in any way.
                */
                public MutationQuery customerAccessTokenCreate(CustomerAccessTokenCreateInput input, CustomerAccessTokenCreatePayloadQueryDefinition queryDef) {
                    startField("customerAccessTokenCreate");

                    _queryBuilder.append("(input:");
                    input.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerAccessTokenCreatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Permanently destroys a customer access token.
                */
                public MutationQuery customerAccessTokenDelete(String customerAccessToken, CustomerAccessTokenDeletePayloadQueryDefinition queryDef) {
                    startField("customerAccessTokenDelete");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerAccessTokenDeletePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Renews a customer access token.
                * Access token renewal must happen *before* a token expires.
                * If a token has already expired, a new one should be created instead via `customerAccessTokenCreate`.
                */
                public MutationQuery customerAccessTokenRenew(String customerAccessToken, CustomerAccessTokenRenewPayloadQueryDefinition queryDef) {
                    startField("customerAccessTokenRenew");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerAccessTokenRenewPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Activates a customer.
                */
                public MutationQuery customerActivate(ID id, CustomerActivateInput input, CustomerActivatePayloadQueryDefinition queryDef) {
                    startField("customerActivate");

                    _queryBuilder.append("(id:");
                    Query.appendQuotedString(_queryBuilder, id.toString());

                    _queryBuilder.append(",input:");
                    input.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerActivatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Creates a new address for a customer.
                */
                public MutationQuery customerAddressCreate(String customerAccessToken, MailingAddressInput address, CustomerAddressCreatePayloadQueryDefinition queryDef) {
                    startField("customerAddressCreate");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(",address:");
                    address.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerAddressCreatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Permanently deletes the address of an existing customer.
                */
                public MutationQuery customerAddressDelete(ID id, String customerAccessToken, CustomerAddressDeletePayloadQueryDefinition queryDef) {
                    startField("customerAddressDelete");

                    _queryBuilder.append("(id:");
                    Query.appendQuotedString(_queryBuilder, id.toString());

                    _queryBuilder.append(",customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerAddressDeletePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates the address of an existing customer.
                */
                public MutationQuery customerAddressUpdate(String customerAccessToken, ID id, MailingAddressInput address, CustomerAddressUpdatePayloadQueryDefinition queryDef) {
                    startField("customerAddressUpdate");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(",id:");
                    Query.appendQuotedString(_queryBuilder, id.toString());

                    _queryBuilder.append(",address:");
                    address.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerAddressUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Creates a new customer.
                */
                public MutationQuery customerCreate(CustomerCreateInput input, CustomerCreatePayloadQueryDefinition queryDef) {
                    startField("customerCreate");

                    _queryBuilder.append("(input:");
                    input.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerCreatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates the default address of an existing customer.
                */
                public MutationQuery customerDefaultAddressUpdate(String customerAccessToken, ID addressId, CustomerDefaultAddressUpdatePayloadQueryDefinition queryDef) {
                    startField("customerDefaultAddressUpdate");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(",addressId:");
                    Query.appendQuotedString(_queryBuilder, addressId.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerDefaultAddressUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Sends a reset password email to the customer, as the first step in the reset password process.
                */
                public MutationQuery customerRecover(String email, CustomerRecoverPayloadQueryDefinition queryDef) {
                    startField("customerRecover");

                    _queryBuilder.append("(email:");
                    Query.appendQuotedString(_queryBuilder, email.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerRecoverPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Resets a customer’s password with a token received from `CustomerRecover`.
                */
                public MutationQuery customerReset(ID id, CustomerResetInput input, CustomerResetPayloadQueryDefinition queryDef) {
                    startField("customerReset");

                    _queryBuilder.append("(id:");
                    Query.appendQuotedString(_queryBuilder, id.toString());

                    _queryBuilder.append(",input:");
                    input.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerResetPayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Updates an existing customer.
                */
                public MutationQuery customerUpdate(String customerAccessToken, CustomerUpdateInput customer, CustomerUpdatePayloadQueryDefinition queryDef) {
                    startField("customerUpdate");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(",customer:");
                    customer.appendTo(_queryBuilder);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerUpdatePayloadQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public String toString() {
                    return _queryBuilder.toString();
                }
            }

            /**
            * The schema’s entry-point for mutations. This acts as the public, top-level API from which all
            * mutation queries must start.
            */
            public static class Mutation extends AbstractResponse<Mutation> {
                public Mutation() {
                }

                public Mutation(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "checkoutAttributesUpdate": {
                                CheckoutAttributesUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutAttributesUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutCompleteFree": {
                                CheckoutCompleteFreePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutCompleteFreePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutCompleteWithCreditCard": {
                                CheckoutCompleteWithCreditCardPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutCompleteWithCreditCardPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutCompleteWithTokenizedPayment": {
                                CheckoutCompleteWithTokenizedPaymentPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutCompleteWithTokenizedPaymentPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutCreate": {
                                CheckoutCreatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutCreatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutCustomerAssociate": {
                                CheckoutCustomerAssociatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutCustomerAssociatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutCustomerDisassociate": {
                                CheckoutCustomerDisassociatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutCustomerDisassociatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutDiscountCodeApply": {
                                CheckoutDiscountCodeApplyPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutDiscountCodeApplyPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutDiscountCodeRemove": {
                                CheckoutDiscountCodeRemovePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutDiscountCodeRemovePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutEmailUpdate": {
                                CheckoutEmailUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutEmailUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutGiftCardApply": {
                                CheckoutGiftCardApplyPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutGiftCardApplyPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutGiftCardRemove": {
                                CheckoutGiftCardRemovePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutGiftCardRemovePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutGiftCardsAppend": {
                                CheckoutGiftCardsAppendPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutGiftCardsAppendPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutLineItemsAdd": {
                                CheckoutLineItemsAddPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutLineItemsAddPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutLineItemsRemove": {
                                CheckoutLineItemsRemovePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutLineItemsRemovePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutLineItemsUpdate": {
                                CheckoutLineItemsUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutLineItemsUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutShippingAddressUpdate": {
                                CheckoutShippingAddressUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutShippingAddressUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkoutShippingLineUpdate": {
                                CheckoutShippingLineUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CheckoutShippingLineUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerAccessTokenCreate": {
                                CustomerAccessTokenCreatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerAccessTokenCreatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerAccessTokenDelete": {
                                CustomerAccessTokenDeletePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerAccessTokenDeletePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerAccessTokenRenew": {
                                CustomerAccessTokenRenewPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerAccessTokenRenewPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerActivate": {
                                CustomerActivatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerActivatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerAddressCreate": {
                                CustomerAddressCreatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerAddressCreatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerAddressDelete": {
                                CustomerAddressDeletePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerAddressDeletePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerAddressUpdate": {
                                CustomerAddressUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerAddressUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerCreate": {
                                CustomerCreatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerCreatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerDefaultAddressUpdate": {
                                CustomerDefaultAddressUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerDefaultAddressUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerRecover": {
                                CustomerRecoverPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerRecoverPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerReset": {
                                CustomerResetPayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerResetPayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerUpdate": {
                                CustomerUpdatePayload optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CustomerUpdatePayload(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "Mutation";
                }

                /**
                * Updates the attributes of a checkout.
                */

                public CheckoutAttributesUpdatePayload getCheckoutAttributesUpdate() {
                    return (CheckoutAttributesUpdatePayload) get("checkoutAttributesUpdate");
                }

                public Mutation setCheckoutAttributesUpdate(CheckoutAttributesUpdatePayload arg) {
                    optimisticData.put(getKey("checkoutAttributesUpdate"), arg);
                    return this;
                }

                public CheckoutCompleteFreePayload getCheckoutCompleteFree() {
                    return (CheckoutCompleteFreePayload) get("checkoutCompleteFree");
                }

                public Mutation setCheckoutCompleteFree(CheckoutCompleteFreePayload arg) {
                    optimisticData.put(getKey("checkoutCompleteFree"), arg);
                    return this;
                }

                /**
                * Completes a checkout using a credit card token from Shopify's Vault.
                */

                public CheckoutCompleteWithCreditCardPayload getCheckoutCompleteWithCreditCard() {
                    return (CheckoutCompleteWithCreditCardPayload) get("checkoutCompleteWithCreditCard");
                }

                public Mutation setCheckoutCompleteWithCreditCard(CheckoutCompleteWithCreditCardPayload arg) {
                    optimisticData.put(getKey("checkoutCompleteWithCreditCard"), arg);
                    return this;
                }

                /**
                * Completes a checkout with a tokenized payment.
                */

                public CheckoutCompleteWithTokenizedPaymentPayload getCheckoutCompleteWithTokenizedPayment() {
                    return (CheckoutCompleteWithTokenizedPaymentPayload) get("checkoutCompleteWithTokenizedPayment");
                }

                public Mutation setCheckoutCompleteWithTokenizedPayment(CheckoutCompleteWithTokenizedPaymentPayload arg) {
                    optimisticData.put(getKey("checkoutCompleteWithTokenizedPayment"), arg);
                    return this;
                }

                /**
                * Creates a new checkout.
                */

                public CheckoutCreatePayload getCheckoutCreate() {
                    return (CheckoutCreatePayload) get("checkoutCreate");
                }

                public Mutation setCheckoutCreate(CheckoutCreatePayload arg) {
                    optimisticData.put(getKey("checkoutCreate"), arg);
                    return this;
                }

                /**
                * Associates a customer to the checkout.
                */

                public CheckoutCustomerAssociatePayload getCheckoutCustomerAssociate() {
                    return (CheckoutCustomerAssociatePayload) get("checkoutCustomerAssociate");
                }

                public Mutation setCheckoutCustomerAssociate(CheckoutCustomerAssociatePayload arg) {
                    optimisticData.put(getKey("checkoutCustomerAssociate"), arg);
                    return this;
                }

                /**
                * Disassociates the current checkout customer from the checkout.
                */

                public CheckoutCustomerDisassociatePayload getCheckoutCustomerDisassociate() {
                    return (CheckoutCustomerDisassociatePayload) get("checkoutCustomerDisassociate");
                }

                public Mutation setCheckoutCustomerDisassociate(CheckoutCustomerDisassociatePayload arg) {
                    optimisticData.put(getKey("checkoutCustomerDisassociate"), arg);
                    return this;
                }

                /**
                * Applies a discount to an existing checkout using a discount code.
                */

                public CheckoutDiscountCodeApplyPayload getCheckoutDiscountCodeApply() {
                    return (CheckoutDiscountCodeApplyPayload) get("checkoutDiscountCodeApply");
                }

                public Mutation setCheckoutDiscountCodeApply(CheckoutDiscountCodeApplyPayload arg) {
                    optimisticData.put(getKey("checkoutDiscountCodeApply"), arg);
                    return this;
                }

                /**
                * Removes the applied discount from an existing checkout.
                */

                public CheckoutDiscountCodeRemovePayload getCheckoutDiscountCodeRemove() {
                    return (CheckoutDiscountCodeRemovePayload) get("checkoutDiscountCodeRemove");
                }

                public Mutation setCheckoutDiscountCodeRemove(CheckoutDiscountCodeRemovePayload arg) {
                    optimisticData.put(getKey("checkoutDiscountCodeRemove"), arg);
                    return this;
                }

                /**
                * Updates the email on an existing checkout.
                */

                public CheckoutEmailUpdatePayload getCheckoutEmailUpdate() {
                    return (CheckoutEmailUpdatePayload) get("checkoutEmailUpdate");
                }

                public Mutation setCheckoutEmailUpdate(CheckoutEmailUpdatePayload arg) {
                    optimisticData.put(getKey("checkoutEmailUpdate"), arg);
                    return this;
                }

                /**
                * Applies a gift card to an existing checkout using a gift card code. This will replace all currently
                * applied gift cards.
                *
                * @deprecated Use `checkoutGiftCardsAppend` instead
                */

                public CheckoutGiftCardApplyPayload getCheckoutGiftCardApply() {
                    return (CheckoutGiftCardApplyPayload) get("checkoutGiftCardApply");
                }

                public Mutation setCheckoutGiftCardApply(CheckoutGiftCardApplyPayload arg) {
                    optimisticData.put(getKey("checkoutGiftCardApply"), arg);
                    return this;
                }

                /**
                * Removes an applied gift card from the checkout.
                */

                public CheckoutGiftCardRemovePayload getCheckoutGiftCardRemove() {
                    return (CheckoutGiftCardRemovePayload) get("checkoutGiftCardRemove");
                }

                public Mutation setCheckoutGiftCardRemove(CheckoutGiftCardRemovePayload arg) {
                    optimisticData.put(getKey("checkoutGiftCardRemove"), arg);
                    return this;
                }

                /**
                * Appends gift cards to an existing checkout.
                */

                public CheckoutGiftCardsAppendPayload getCheckoutGiftCardsAppend() {
                    return (CheckoutGiftCardsAppendPayload) get("checkoutGiftCardsAppend");
                }

                public Mutation setCheckoutGiftCardsAppend(CheckoutGiftCardsAppendPayload arg) {
                    optimisticData.put(getKey("checkoutGiftCardsAppend"), arg);
                    return this;
                }

                /**
                * Adds a list of line items to a checkout.
                */

                public CheckoutLineItemsAddPayload getCheckoutLineItemsAdd() {
                    return (CheckoutLineItemsAddPayload) get("checkoutLineItemsAdd");
                }

                public Mutation setCheckoutLineItemsAdd(CheckoutLineItemsAddPayload arg) {
                    optimisticData.put(getKey("checkoutLineItemsAdd"), arg);
                    return this;
                }

                /**
                * Removes line items from an existing checkout
                */

                public CheckoutLineItemsRemovePayload getCheckoutLineItemsRemove() {
                    return (CheckoutLineItemsRemovePayload) get("checkoutLineItemsRemove");
                }

                public Mutation setCheckoutLineItemsRemove(CheckoutLineItemsRemovePayload arg) {
                    optimisticData.put(getKey("checkoutLineItemsRemove"), arg);
                    return this;
                }

                /**
                * Updates line items on a checkout.
                */

                public CheckoutLineItemsUpdatePayload getCheckoutLineItemsUpdate() {
                    return (CheckoutLineItemsUpdatePayload) get("checkoutLineItemsUpdate");
                }

                public Mutation setCheckoutLineItemsUpdate(CheckoutLineItemsUpdatePayload arg) {
                    optimisticData.put(getKey("checkoutLineItemsUpdate"), arg);
                    return this;
                }

                /**
                * Updates the shipping address of an existing checkout.
                */

                public CheckoutShippingAddressUpdatePayload getCheckoutShippingAddressUpdate() {
                    return (CheckoutShippingAddressUpdatePayload) get("checkoutShippingAddressUpdate");
                }

                public Mutation setCheckoutShippingAddressUpdate(CheckoutShippingAddressUpdatePayload arg) {
                    optimisticData.put(getKey("checkoutShippingAddressUpdate"), arg);
                    return this;
                }

                /**
                * Updates the shipping lines on an existing checkout.
                */

                public CheckoutShippingLineUpdatePayload getCheckoutShippingLineUpdate() {
                    return (CheckoutShippingLineUpdatePayload) get("checkoutShippingLineUpdate");
                }

                public Mutation setCheckoutShippingLineUpdate(CheckoutShippingLineUpdatePayload arg) {
                    optimisticData.put(getKey("checkoutShippingLineUpdate"), arg);
                    return this;
                }

                /**
                * Creates a customer access token.
                * The customer access token is required to modify the customer object in any way.
                */

                public CustomerAccessTokenCreatePayload getCustomerAccessTokenCreate() {
                    return (CustomerAccessTokenCreatePayload) get("customerAccessTokenCreate");
                }

                public Mutation setCustomerAccessTokenCreate(CustomerAccessTokenCreatePayload arg) {
                    optimisticData.put(getKey("customerAccessTokenCreate"), arg);
                    return this;
                }

                /**
                * Permanently destroys a customer access token.
                */

                public CustomerAccessTokenDeletePayload getCustomerAccessTokenDelete() {
                    return (CustomerAccessTokenDeletePayload) get("customerAccessTokenDelete");
                }

                public Mutation setCustomerAccessTokenDelete(CustomerAccessTokenDeletePayload arg) {
                    optimisticData.put(getKey("customerAccessTokenDelete"), arg);
                    return this;
                }

                /**
                * Renews a customer access token.
                * Access token renewal must happen *before* a token expires.
                * If a token has already expired, a new one should be created instead via `customerAccessTokenCreate`.
                */

                public CustomerAccessTokenRenewPayload getCustomerAccessTokenRenew() {
                    return (CustomerAccessTokenRenewPayload) get("customerAccessTokenRenew");
                }

                public Mutation setCustomerAccessTokenRenew(CustomerAccessTokenRenewPayload arg) {
                    optimisticData.put(getKey("customerAccessTokenRenew"), arg);
                    return this;
                }

                /**
                * Activates a customer.
                */

                public CustomerActivatePayload getCustomerActivate() {
                    return (CustomerActivatePayload) get("customerActivate");
                }

                public Mutation setCustomerActivate(CustomerActivatePayload arg) {
                    optimisticData.put(getKey("customerActivate"), arg);
                    return this;
                }

                /**
                * Creates a new address for a customer.
                */

                public CustomerAddressCreatePayload getCustomerAddressCreate() {
                    return (CustomerAddressCreatePayload) get("customerAddressCreate");
                }

                public Mutation setCustomerAddressCreate(CustomerAddressCreatePayload arg) {
                    optimisticData.put(getKey("customerAddressCreate"), arg);
                    return this;
                }

                /**
                * Permanently deletes the address of an existing customer.
                */

                public CustomerAddressDeletePayload getCustomerAddressDelete() {
                    return (CustomerAddressDeletePayload) get("customerAddressDelete");
                }

                public Mutation setCustomerAddressDelete(CustomerAddressDeletePayload arg) {
                    optimisticData.put(getKey("customerAddressDelete"), arg);
                    return this;
                }

                /**
                * Updates the address of an existing customer.
                */

                public CustomerAddressUpdatePayload getCustomerAddressUpdate() {
                    return (CustomerAddressUpdatePayload) get("customerAddressUpdate");
                }

                public Mutation setCustomerAddressUpdate(CustomerAddressUpdatePayload arg) {
                    optimisticData.put(getKey("customerAddressUpdate"), arg);
                    return this;
                }

                /**
                * Creates a new customer.
                */

                public CustomerCreatePayload getCustomerCreate() {
                    return (CustomerCreatePayload) get("customerCreate");
                }

                public Mutation setCustomerCreate(CustomerCreatePayload arg) {
                    optimisticData.put(getKey("customerCreate"), arg);
                    return this;
                }

                /**
                * Updates the default address of an existing customer.
                */

                public CustomerDefaultAddressUpdatePayload getCustomerDefaultAddressUpdate() {
                    return (CustomerDefaultAddressUpdatePayload) get("customerDefaultAddressUpdate");
                }

                public Mutation setCustomerDefaultAddressUpdate(CustomerDefaultAddressUpdatePayload arg) {
                    optimisticData.put(getKey("customerDefaultAddressUpdate"), arg);
                    return this;
                }

                /**
                * Sends a reset password email to the customer, as the first step in the reset password process.
                */

                public CustomerRecoverPayload getCustomerRecover() {
                    return (CustomerRecoverPayload) get("customerRecover");
                }

                public Mutation setCustomerRecover(CustomerRecoverPayload arg) {
                    optimisticData.put(getKey("customerRecover"), arg);
                    return this;
                }

                /**
                * Resets a customer’s password with a token received from `CustomerRecover`.
                */

                public CustomerResetPayload getCustomerReset() {
                    return (CustomerResetPayload) get("customerReset");
                }

                public Mutation setCustomerReset(CustomerResetPayload arg) {
                    optimisticData.put(getKey("customerReset"), arg);
                    return this;
                }

                /**
                * Updates an existing customer.
                */

                public CustomerUpdatePayload getCustomerUpdate() {
                    return (CustomerUpdatePayload) get("customerUpdate");
                }

                public Mutation setCustomerUpdate(CustomerUpdatePayload arg) {
                    optimisticData.put(getKey("customerUpdate"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "checkoutAttributesUpdate": return true;

                        case "checkoutCompleteFree": return true;

                        case "checkoutCompleteWithCreditCard": return true;

                        case "checkoutCompleteWithTokenizedPayment": return true;

                        case "checkoutCreate": return true;

                        case "checkoutCustomerAssociate": return true;

                        case "checkoutCustomerDisassociate": return true;

                        case "checkoutDiscountCodeApply": return true;

                        case "checkoutDiscountCodeRemove": return true;

                        case "checkoutEmailUpdate": return true;

                        case "checkoutGiftCardApply": return true;

                        case "checkoutGiftCardRemove": return true;

                        case "checkoutGiftCardsAppend": return true;

                        case "checkoutLineItemsAdd": return true;

                        case "checkoutLineItemsRemove": return true;

                        case "checkoutLineItemsUpdate": return true;

                        case "checkoutShippingAddressUpdate": return true;

                        case "checkoutShippingLineUpdate": return true;

                        case "customerAccessTokenCreate": return true;

                        case "customerAccessTokenDelete": return true;

                        case "customerAccessTokenRenew": return true;

                        case "customerActivate": return true;

                        case "customerAddressCreate": return true;

                        case "customerAddressDelete": return true;

                        case "customerAddressUpdate": return true;

                        case "customerCreate": return true;

                        case "customerDefaultAddressUpdate": return true;

                        case "customerRecover": return true;

                        case "customerReset": return true;

                        case "customerUpdate": return true;

                        default: return false;
                    }
                }
            }

            public interface NodeQueryDefinition {
                void define(NodeQuery _queryBuilder);
            }

            /**
            * An object with an ID to support global identification.
            */
            public static class NodeQuery extends Query<NodeQuery> {
                NodeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("__typename");
                }

                /**
                * Globally unique identifier.
                */
                public NodeQuery id() {
                    startField("id");

                    return this;
                }

                public NodeQuery onAppliedGiftCard(AppliedGiftCardQueryDefinition queryDef) {
                    startInlineFragment("AppliedGiftCard");
                    queryDef.define(new AppliedGiftCardQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onArticle(ArticleQueryDefinition queryDef) {
                    startInlineFragment("Article");
                    queryDef.define(new ArticleQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onBlog(BlogQueryDefinition queryDef) {
                    startInlineFragment("Blog");
                    queryDef.define(new BlogQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onCheckout(CheckoutQueryDefinition queryDef) {
                    startInlineFragment("Checkout");
                    queryDef.define(new CheckoutQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onCheckoutLineItem(CheckoutLineItemQueryDefinition queryDef) {
                    startInlineFragment("CheckoutLineItem");
                    queryDef.define(new CheckoutLineItemQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onCollection(CollectionQueryDefinition queryDef) {
                    startInlineFragment("Collection");
                    queryDef.define(new CollectionQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onComment(CommentQueryDefinition queryDef) {
                    startInlineFragment("Comment");
                    queryDef.define(new CommentQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onMailingAddress(MailingAddressQueryDefinition queryDef) {
                    startInlineFragment("MailingAddress");
                    queryDef.define(new MailingAddressQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onOrder(OrderQueryDefinition queryDef) {
                    startInlineFragment("Order");
                    queryDef.define(new OrderQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onPayment(PaymentQueryDefinition queryDef) {
                    startInlineFragment("Payment");
                    queryDef.define(new PaymentQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onProduct(ProductQueryDefinition queryDef) {
                    startInlineFragment("Product");
                    queryDef.define(new ProductQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onProductOption(ProductOptionQueryDefinition queryDef) {
                    startInlineFragment("ProductOption");
                    queryDef.define(new ProductOptionQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onProductVariant(ProductVariantQueryDefinition queryDef) {
                    startInlineFragment("ProductVariant");
                    queryDef.define(new ProductVariantQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }

                public NodeQuery onShopPolicy(ShopPolicyQueryDefinition queryDef) {
                    startInlineFragment("ShopPolicy");
                    queryDef.define(new ShopPolicyQuery(_queryBuilder));
                    _queryBuilder.append('}');
                    return this;
                }
            }

            public interface Node extends com.shopify.graphql.support.Node {
                String getGraphQlTypeName();

                ID getId();
            }

            /**
            * An object with an ID to support global identification.
            */
            public static class UnknownNode extends AbstractResponse<UnknownNode> implements Node {
                public UnknownNode() {
                }

                public UnknownNode(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public static Node create(JsonObject fields) throws SchemaViolationError {
                    String typeName = fields.getAsJsonPrimitive("__typename").getAsString();
                    switch (typeName) {
                        case "AppliedGiftCard": {
                            return new AppliedGiftCard(fields);
                        }

                        case "Article": {
                            return new Article(fields);
                        }

                        case "Blog": {
                            return new Blog(fields);
                        }

                        case "Checkout": {
                            return new Checkout(fields);
                        }

                        case "CheckoutLineItem": {
                            return new CheckoutLineItem(fields);
                        }

                        case "Collection": {
                            return new Collection(fields);
                        }

                        case "Comment": {
                            return new Comment(fields);
                        }

                        case "MailingAddress": {
                            return new MailingAddress(fields);
                        }

                        case "Order": {
                            return new Order(fields);
                        }

                        case "Payment": {
                            return new Payment(fields);
                        }

                        case "Product": {
                            return new Product(fields);
                        }

                        case "ProductOption": {
                            return new ProductOption(fields);
                        }

                        case "ProductVariant": {
                            return new ProductVariant(fields);
                        }

                        case "ShopPolicy": {
                            return new ShopPolicy(fields);
                        }

                        default: {
                            return new UnknownNode(fields);
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return (String) get("__typename");
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                public UnknownNode setId(ID arg) {
                    optimisticData.put(getKey("id"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "id": return false;

                        default: return false;
                    }
                }
            }

            public interface OrderQueryDefinition {
                void define(OrderQuery _queryBuilder);
            }

            /**
            * An order is a customer’s completed request to purchase one or more products from a shop. An order is
            * created when a customer completes the checkout process, during which time they provides an email
            * address, billing address and payment information.
            */
            public static class OrderQuery extends Query<OrderQuery> {
                OrderQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("id");
                }

                /**
                * The code of the currency used for the payment.
                */
                public OrderQuery currencyCode() {
                    startField("currencyCode");

                    return this;
                }

                /**
                * The locale code in which this specific order happened.
                */
                public OrderQuery customerLocale() {
                    startField("customerLocale");

                    return this;
                }

                /**
                * The unique URL that the customer can use to access the order.
                */
                public OrderQuery customerUrl() {
                    startField("customerUrl");

                    return this;
                }

                /**
                * The customer's email address.
                */
                public OrderQuery email() {
                    startField("email");

                    return this;
                }

                public class LineItemsArguments extends Arguments {
                    LineItemsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public LineItemsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public LineItemsArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public LineItemsArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public LineItemsArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public LineItemsArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface LineItemsArgumentsDefinition {
                    void define(LineItemsArguments args);
                }

                /**
                * List of the order’s line items.
                */
                public OrderQuery lineItems(OrderLineItemConnectionQueryDefinition queryDef) {
                    return lineItems(args -> {}, queryDef);
                }

                /**
                * List of the order’s line items.
                */
                public OrderQuery lineItems(LineItemsArgumentsDefinition argsDef, OrderLineItemConnectionQueryDefinition queryDef) {
                    startField("lineItems");

                    LineItemsArguments args = new LineItemsArguments(_queryBuilder);
                    argsDef.define(args);
                    LineItemsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new OrderLineItemConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * A unique numeric identifier for the order for use by shop owner and customer.
                */
                public OrderQuery orderNumber() {
                    startField("orderNumber");

                    return this;
                }

                /**
                * The customer's phone number.
                */
                public OrderQuery phone() {
                    startField("phone");

                    return this;
                }

                /**
                * The date and time when the order was imported.
                * This value can be set to dates in the past when importing from other systems.
                * If no value is provided, it will be auto-generated based on current date and time.
                */
                public OrderQuery processedAt() {
                    startField("processedAt");

                    return this;
                }

                /**
                * The address to where the order will be shipped.
                */
                public OrderQuery shippingAddress(MailingAddressQueryDefinition queryDef) {
                    startField("shippingAddress");

                    _queryBuilder.append('{');
                    queryDef.define(new MailingAddressQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The unique URL for the order's status page.
                */
                public OrderQuery statusUrl() {
                    startField("statusUrl");

                    return this;
                }

                /**
                * Price of the order before shipping and taxes.
                */
                public OrderQuery subtotalPrice() {
                    startField("subtotalPrice");

                    return this;
                }

                public class SuccessfulFulfillmentsArguments extends Arguments {
                    SuccessfulFulfillmentsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Truncate the array result to this size.
                    */
                    public SuccessfulFulfillmentsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface SuccessfulFulfillmentsArgumentsDefinition {
                    void define(SuccessfulFulfillmentsArguments args);
                }

                /**
                * List of the order’s successful fulfillments.
                */
                public OrderQuery successfulFulfillments(FulfillmentQueryDefinition queryDef) {
                    return successfulFulfillments(args -> {}, queryDef);
                }

                /**
                * List of the order’s successful fulfillments.
                */
                public OrderQuery successfulFulfillments(SuccessfulFulfillmentsArgumentsDefinition argsDef, FulfillmentQueryDefinition queryDef) {
                    startField("successfulFulfillments");

                    SuccessfulFulfillmentsArguments args = new SuccessfulFulfillmentsArguments(_queryBuilder);
                    argsDef.define(args);
                    SuccessfulFulfillmentsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new FulfillmentQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The sum of all the prices of all the items in the order, taxes and discounts included (must be
                * positive).
                */
                public OrderQuery totalPrice() {
                    startField("totalPrice");

                    return this;
                }

                /**
                * The total amount that has been refunded.
                */
                public OrderQuery totalRefunded() {
                    startField("totalRefunded");

                    return this;
                }

                /**
                * The total cost of shipping.
                */
                public OrderQuery totalShippingPrice() {
                    startField("totalShippingPrice");

                    return this;
                }

                /**
                * The total cost of taxes.
                */
                public OrderQuery totalTax() {
                    startField("totalTax");

                    return this;
                }
            }

            /**
            * An order is a customer’s completed request to purchase one or more products from a shop. An order is
            * created when a customer completes the checkout process, during which time they provides an email
            * address, billing address and payment information.
            */
            public static class Order extends AbstractResponse<Order> implements Node {
                public Order() {
                }

                public Order(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "currencyCode": {
                                responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "customerLocale": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "customerUrl": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "email": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "lineItems": {
                                responseData.put(key, new OrderLineItemConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "orderNumber": {
                                responseData.put(key, jsonAsInteger(field.getValue(), key));

                                break;
                            }

                            case "phone": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "processedAt": {
                                responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "shippingAddress": {
                                MailingAddress optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "statusUrl": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "subtotalPrice": {
                                BigDecimal optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new BigDecimal(jsonAsString(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "successfulFulfillments": {
                                List<Fulfillment> optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    List<Fulfillment> list1 = new ArrayList<>();
                                    for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                        list1.add(new Fulfillment(jsonAsObject(element1, key)));
                                    }

                                    optional1 = list1;
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "totalPrice": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "totalRefunded": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "totalShippingPrice": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "totalTax": {
                                BigDecimal optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new BigDecimal(jsonAsString(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public Order(ID id) {
                    this();
                    optimisticData.put("id", id);
                }

                public String getGraphQlTypeName() {
                    return "Order";
                }

                /**
                * The code of the currency used for the payment.
                */

                public CurrencyCode getCurrencyCode() {
                    return (CurrencyCode) get("currencyCode");
                }

                public Order setCurrencyCode(CurrencyCode arg) {
                    optimisticData.put(getKey("currencyCode"), arg);
                    return this;
                }

                /**
                * The locale code in which this specific order happened.
                */

                public String getCustomerLocale() {
                    return (String) get("customerLocale");
                }

                public Order setCustomerLocale(String arg) {
                    optimisticData.put(getKey("customerLocale"), arg);
                    return this;
                }

                /**
                * The unique URL that the customer can use to access the order.
                */

                public String getCustomerUrl() {
                    return (String) get("customerUrl");
                }

                public Order setCustomerUrl(String arg) {
                    optimisticData.put(getKey("customerUrl"), arg);
                    return this;
                }

                /**
                * The customer's email address.
                */

                public String getEmail() {
                    return (String) get("email");
                }

                public Order setEmail(String arg) {
                    optimisticData.put(getKey("email"), arg);
                    return this;
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                /**
                * List of the order’s line items.
                */

                public OrderLineItemConnection getLineItems() {
                    return (OrderLineItemConnection) get("lineItems");
                }

                public Order setLineItems(OrderLineItemConnection arg) {
                    optimisticData.put(getKey("lineItems"), arg);
                    return this;
                }

                /**
                * A unique numeric identifier for the order for use by shop owner and customer.
                */

                public Integer getOrderNumber() {
                    return (Integer) get("orderNumber");
                }

                public Order setOrderNumber(Integer arg) {
                    optimisticData.put(getKey("orderNumber"), arg);
                    return this;
                }

                /**
                * The customer's phone number.
                */

                public String getPhone() {
                    return (String) get("phone");
                }

                public Order setPhone(String arg) {
                    optimisticData.put(getKey("phone"), arg);
                    return this;
                }

                /**
                * The date and time when the order was imported.
                * This value can be set to dates in the past when importing from other systems.
                * If no value is provided, it will be auto-generated based on current date and time.
                */

                public DateTime getProcessedAt() {
                    return (DateTime) get("processedAt");
                }

                public Order setProcessedAt(DateTime arg) {
                    optimisticData.put(getKey("processedAt"), arg);
                    return this;
                }

                /**
                * The address to where the order will be shipped.
                */

                public MailingAddress getShippingAddress() {
                    return (MailingAddress) get("shippingAddress");
                }

                public Order setShippingAddress(MailingAddress arg) {
                    optimisticData.put(getKey("shippingAddress"), arg);
                    return this;
                }

                /**
                * The unique URL for the order's status page.
                */

                public String getStatusUrl() {
                    return (String) get("statusUrl");
                }

                public Order setStatusUrl(String arg) {
                    optimisticData.put(getKey("statusUrl"), arg);
                    return this;
                }

                /**
                * Price of the order before shipping and taxes.
                */

                public BigDecimal getSubtotalPrice() {
                    return (BigDecimal) get("subtotalPrice");
                }

                public Order setSubtotalPrice(BigDecimal arg) {
                    optimisticData.put(getKey("subtotalPrice"), arg);
                    return this;
                }

                /**
                * List of the order’s successful fulfillments.
                */

                public List<Fulfillment> getSuccessfulFulfillments() {
                    return (List<Fulfillment>) get("successfulFulfillments");
                }

                public Order setSuccessfulFulfillments(List<Fulfillment> arg) {
                    optimisticData.put(getKey("successfulFulfillments"), arg);
                    return this;
                }

                /**
                * The sum of all the prices of all the items in the order, taxes and discounts included (must be
                * positive).
                */

                public BigDecimal getTotalPrice() {
                    return (BigDecimal) get("totalPrice");
                }

                public Order setTotalPrice(BigDecimal arg) {
                    optimisticData.put(getKey("totalPrice"), arg);
                    return this;
                }

                /**
                * The total amount that has been refunded.
                */

                public BigDecimal getTotalRefunded() {
                    return (BigDecimal) get("totalRefunded");
                }

                public Order setTotalRefunded(BigDecimal arg) {
                    optimisticData.put(getKey("totalRefunded"), arg);
                    return this;
                }

                /**
                * The total cost of shipping.
                */

                public BigDecimal getTotalShippingPrice() {
                    return (BigDecimal) get("totalShippingPrice");
                }

                public Order setTotalShippingPrice(BigDecimal arg) {
                    optimisticData.put(getKey("totalShippingPrice"), arg);
                    return this;
                }

                /**
                * The total cost of taxes.
                */

                public BigDecimal getTotalTax() {
                    return (BigDecimal) get("totalTax");
                }

                public Order setTotalTax(BigDecimal arg) {
                    optimisticData.put(getKey("totalTax"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "currencyCode": return false;

                        case "customerLocale": return false;

                        case "customerUrl": return false;

                        case "email": return false;

                        case "id": return false;

                        case "lineItems": return true;

                        case "orderNumber": return false;

                        case "phone": return false;

                        case "processedAt": return false;

                        case "shippingAddress": return true;

                        case "statusUrl": return false;

                        case "subtotalPrice": return false;

                        case "successfulFulfillments": return true;

                        case "totalPrice": return false;

                        case "totalRefunded": return false;

                        case "totalShippingPrice": return false;

                        case "totalTax": return false;

                        default: return false;
                    }
                }
            }

            public interface OrderConnectionQueryDefinition {
                void define(OrderConnectionQuery _queryBuilder);
            }

            public static class OrderConnectionQuery extends Query<OrderConnectionQuery> {
                OrderConnectionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A list of edges.
                */
                public OrderConnectionQuery edges(OrderEdgeQueryDefinition queryDef) {
                    startField("edges");

                    _queryBuilder.append('{');
                    queryDef.define(new OrderEdgeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Information to aid in pagination.
                */
                public OrderConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
                    startField("pageInfo");

                    _queryBuilder.append('{');
                    queryDef.define(new PageInfoQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class OrderConnection extends AbstractResponse<OrderConnection> {
                public OrderConnection() {
                }

                public OrderConnection(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "edges": {
                                List<OrderEdge> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new OrderEdge(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "pageInfo": {
                                responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "OrderConnection";
                }

                /**
                * A list of edges.
                */

                public List<OrderEdge> getEdges() {
                    return (List<OrderEdge>) get("edges");
                }

                public OrderConnection setEdges(List<OrderEdge> arg) {
                    optimisticData.put(getKey("edges"), arg);
                    return this;
                }

                /**
                * Information to aid in pagination.
                */

                public PageInfo getPageInfo() {
                    return (PageInfo) get("pageInfo");
                }

                public OrderConnection setPageInfo(PageInfo arg) {
                    optimisticData.put(getKey("pageInfo"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "edges": return true;

                        case "pageInfo": return true;

                        default: return false;
                    }
                }
            }

            public interface OrderEdgeQueryDefinition {
                void define(OrderEdgeQuery _queryBuilder);
            }

            public static class OrderEdgeQuery extends Query<OrderEdgeQuery> {
                OrderEdgeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A cursor for use in pagination.
                */
                public OrderEdgeQuery cursor() {
                    startField("cursor");

                    return this;
                }

                /**
                * The item at the end of OrderEdge.
                */
                public OrderEdgeQuery node(OrderQueryDefinition queryDef) {
                    startField("node");

                    _queryBuilder.append('{');
                    queryDef.define(new OrderQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class OrderEdge extends AbstractResponse<OrderEdge> {
                public OrderEdge() {
                }

                public OrderEdge(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "cursor": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "node": {
                                responseData.put(key, new Order(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "OrderEdge";
                }

                /**
                * A cursor for use in pagination.
                */

                public String getCursor() {
                    return (String) get("cursor");
                }

                public OrderEdge setCursor(String arg) {
                    optimisticData.put(getKey("cursor"), arg);
                    return this;
                }

                /**
                * The item at the end of OrderEdge.
                */

                public Order getNode() {
                    return (Order) get("node");
                }

                public OrderEdge setNode(Order arg) {
                    optimisticData.put(getKey("node"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "cursor": return false;

                        case "node": return true;

                        default: return false;
                    }
                }
            }

            public interface OrderLineItemQueryDefinition {
                void define(OrderLineItemQuery _queryBuilder);
            }

            /**
            * Represents a single line in an order. There is one line item for each distinct product variant.
            */
            public static class OrderLineItemQuery extends Query<OrderLineItemQuery> {
                OrderLineItemQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * List of custom attributes associated to the line item.
                */
                public OrderLineItemQuery customAttributes(AttributeQueryDefinition queryDef) {
                    startField("customAttributes");

                    _queryBuilder.append('{');
                    queryDef.define(new AttributeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The number of products variants associated to the line item.
                */
                public OrderLineItemQuery quantity() {
                    startField("quantity");

                    return this;
                }

                /**
                * The title of the product combined with title of the variant.
                */
                public OrderLineItemQuery title() {
                    startField("title");

                    return this;
                }

                /**
                * The product variant object associated to the line item.
                */
                public OrderLineItemQuery variant(ProductVariantQueryDefinition queryDef) {
                    startField("variant");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductVariantQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            /**
            * Represents a single line in an order. There is one line item for each distinct product variant.
            */
            public static class OrderLineItem extends AbstractResponse<OrderLineItem> {
                public OrderLineItem() {
                }

                public OrderLineItem(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "customAttributes": {
                                List<Attribute> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new Attribute(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "quantity": {
                                responseData.put(key, jsonAsInteger(field.getValue(), key));

                                break;
                            }

                            case "title": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "variant": {
                                ProductVariant optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new ProductVariant(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "OrderLineItem";
                }

                /**
                * List of custom attributes associated to the line item.
                */

                public List<Attribute> getCustomAttributes() {
                    return (List<Attribute>) get("customAttributes");
                }

                public OrderLineItem setCustomAttributes(List<Attribute> arg) {
                    optimisticData.put(getKey("customAttributes"), arg);
                    return this;
                }

                /**
                * The number of products variants associated to the line item.
                */

                public Integer getQuantity() {
                    return (Integer) get("quantity");
                }

                public OrderLineItem setQuantity(Integer arg) {
                    optimisticData.put(getKey("quantity"), arg);
                    return this;
                }

                /**
                * The title of the product combined with title of the variant.
                */

                public String getTitle() {
                    return (String) get("title");
                }

                public OrderLineItem setTitle(String arg) {
                    optimisticData.put(getKey("title"), arg);
                    return this;
                }

                /**
                * The product variant object associated to the line item.
                */

                public ProductVariant getVariant() {
                    return (ProductVariant) get("variant");
                }

                public OrderLineItem setVariant(ProductVariant arg) {
                    optimisticData.put(getKey("variant"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "customAttributes": return true;

                        case "quantity": return false;

                        case "title": return false;

                        case "variant": return true;

                        default: return false;
                    }
                }
            }

            public interface OrderLineItemConnectionQueryDefinition {
                void define(OrderLineItemConnectionQuery _queryBuilder);
            }

            public static class OrderLineItemConnectionQuery extends Query<OrderLineItemConnectionQuery> {
                OrderLineItemConnectionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A list of edges.
                */
                public OrderLineItemConnectionQuery edges(OrderLineItemEdgeQueryDefinition queryDef) {
                    startField("edges");

                    _queryBuilder.append('{');
                    queryDef.define(new OrderLineItemEdgeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Information to aid in pagination.
                */
                public OrderLineItemConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
                    startField("pageInfo");

                    _queryBuilder.append('{');
                    queryDef.define(new PageInfoQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class OrderLineItemConnection extends AbstractResponse<OrderLineItemConnection> {
                public OrderLineItemConnection() {
                }

                public OrderLineItemConnection(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "edges": {
                                List<OrderLineItemEdge> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new OrderLineItemEdge(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "pageInfo": {
                                responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "OrderLineItemConnection";
                }

                /**
                * A list of edges.
                */

                public List<OrderLineItemEdge> getEdges() {
                    return (List<OrderLineItemEdge>) get("edges");
                }

                public OrderLineItemConnection setEdges(List<OrderLineItemEdge> arg) {
                    optimisticData.put(getKey("edges"), arg);
                    return this;
                }

                /**
                * Information to aid in pagination.
                */

                public PageInfo getPageInfo() {
                    return (PageInfo) get("pageInfo");
                }

                public OrderLineItemConnection setPageInfo(PageInfo arg) {
                    optimisticData.put(getKey("pageInfo"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "edges": return true;

                        case "pageInfo": return true;

                        default: return false;
                    }
                }
            }

            public interface OrderLineItemEdgeQueryDefinition {
                void define(OrderLineItemEdgeQuery _queryBuilder);
            }

            public static class OrderLineItemEdgeQuery extends Query<OrderLineItemEdgeQuery> {
                OrderLineItemEdgeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A cursor for use in pagination.
                */
                public OrderLineItemEdgeQuery cursor() {
                    startField("cursor");

                    return this;
                }

                /**
                * The item at the end of OrderLineItemEdge.
                */
                public OrderLineItemEdgeQuery node(OrderLineItemQueryDefinition queryDef) {
                    startField("node");

                    _queryBuilder.append('{');
                    queryDef.define(new OrderLineItemQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class OrderLineItemEdge extends AbstractResponse<OrderLineItemEdge> {
                public OrderLineItemEdge() {
                }

                public OrderLineItemEdge(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "cursor": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "node": {
                                responseData.put(key, new OrderLineItem(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "OrderLineItemEdge";
                }

                /**
                * A cursor for use in pagination.
                */

                public String getCursor() {
                    return (String) get("cursor");
                }

                public OrderLineItemEdge setCursor(String arg) {
                    optimisticData.put(getKey("cursor"), arg);
                    return this;
                }

                /**
                * The item at the end of OrderLineItemEdge.
                */

                public OrderLineItem getNode() {
                    return (OrderLineItem) get("node");
                }

                public OrderLineItemEdge setNode(OrderLineItem arg) {
                    optimisticData.put(getKey("node"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "cursor": return false;

                        case "node": return true;

                        default: return false;
                    }
                }
            }

            /**
            * The set of valid sort keys for the orders query.
            */
            public enum OrderSortKeys {
                ID,

                PROCESSED_AT,

                /**
                * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
                * the
                * results by relevance to the search term(s). When no search query is specified, this sort key is not
                * deterministic and should not be used.
                */
                RELEVANCE,

                TOTAL_PRICE,

                UNKNOWN_VALUE;

                public static OrderSortKeys fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "ID": {
                            return ID;
                        }

                        case "PROCESSED_AT": {
                            return PROCESSED_AT;
                        }

                        case "RELEVANCE": {
                            return RELEVANCE;
                        }

                        case "TOTAL_PRICE": {
                            return TOTAL_PRICE;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case ID: {
                            return "ID";
                        }

                        case PROCESSED_AT: {
                            return "PROCESSED_AT";
                        }

                        case RELEVANCE: {
                            return "RELEVANCE";
                        }

                        case TOTAL_PRICE: {
                            return "TOTAL_PRICE";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public interface PageInfoQueryDefinition {
                void define(PageInfoQuery _queryBuilder);
            }

            /**
            * Information about pagination in a connection.
            */
            public static class PageInfoQuery extends Query<PageInfoQuery> {
                PageInfoQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * Indicates if there are more pages to fetch.
                */
                public PageInfoQuery hasNextPage() {
                    startField("hasNextPage");

                    return this;
                }

                /**
                * Indicates if there are any pages prior to the current page.
                */
                public PageInfoQuery hasPreviousPage() {
                    startField("hasPreviousPage");

                    return this;
                }
            }

            /**
            * Information about pagination in a connection.
            */
            public static class PageInfo extends AbstractResponse<PageInfo> {
                public PageInfo() {
                }

                public PageInfo(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "hasNextPage": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "hasPreviousPage": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "PageInfo";
                }

                /**
                * Indicates if there are more pages to fetch.
                */

                public Boolean getHasNextPage() {
                    return (Boolean) get("hasNextPage");
                }

                public PageInfo setHasNextPage(Boolean arg) {
                    optimisticData.put(getKey("hasNextPage"), arg);
                    return this;
                }

                /**
                * Indicates if there are any pages prior to the current page.
                */

                public Boolean getHasPreviousPage() {
                    return (Boolean) get("hasPreviousPage");
                }

                public PageInfo setHasPreviousPage(Boolean arg) {
                    optimisticData.put(getKey("hasPreviousPage"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "hasNextPage": return false;

                        case "hasPreviousPage": return false;

                        default: return false;
                    }
                }
            }

            public interface PaymentQueryDefinition {
                void define(PaymentQuery _queryBuilder);
            }

            /**
            * A payment applied to a checkout.
            */
            public static class PaymentQuery extends Query<PaymentQuery> {
                PaymentQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("id");
                }

                /**
                * The amount of the payment.
                */
                public PaymentQuery amount() {
                    startField("amount");

                    return this;
                }

                /**
                * The billing address for the payment.
                */
                public PaymentQuery billingAddress(MailingAddressQueryDefinition queryDef) {
                    startField("billingAddress");

                    _queryBuilder.append('{');
                    queryDef.define(new MailingAddressQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The checkout to which the payment belongs.
                */
                public PaymentQuery checkout(CheckoutQueryDefinition queryDef) {
                    startField("checkout");

                    _queryBuilder.append('{');
                    queryDef.define(new CheckoutQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The credit card used for the payment in the case of direct payments.
                */
                public PaymentQuery creditCard(CreditCardQueryDefinition queryDef) {
                    startField("creditCard");

                    _queryBuilder.append('{');
                    queryDef.define(new CreditCardQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * An message describing a processing error during asynchronous processing.
                */
                public PaymentQuery errorMessage() {
                    startField("errorMessage");

                    return this;
                }

                /**
                * A client-side generated token to identify a payment and perform idempotent operations.
                */
                public PaymentQuery idempotencyKey() {
                    startField("idempotencyKey");

                    return this;
                }

                /**
                * Whether or not the payment is still processing asynchronously.
                */
                public PaymentQuery ready() {
                    startField("ready");

                    return this;
                }

                /**
                * A flag to indicate if the payment is to be done in test mode for gateways that support it.
                */
                public PaymentQuery test() {
                    startField("test");

                    return this;
                }

                /**
                * The actual transaction recorded by Shopify after having processed the payment with the gateway.
                */
                public PaymentQuery transaction(TransactionQueryDefinition queryDef) {
                    startField("transaction");

                    _queryBuilder.append('{');
                    queryDef.define(new TransactionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            /**
            * A payment applied to a checkout.
            */
            public static class Payment extends AbstractResponse<Payment> implements Node {
                public Payment() {
                }

                public Payment(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "amount": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "billingAddress": {
                                MailingAddress optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "checkout": {
                                responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "creditCard": {
                                CreditCard optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new CreditCard(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "errorMessage": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "idempotencyKey": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "ready": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "test": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "transaction": {
                                Transaction optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new Transaction(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public Payment(ID id) {
                    this();
                    optimisticData.put("id", id);
                }

                public String getGraphQlTypeName() {
                    return "Payment";
                }

                /**
                * The amount of the payment.
                */

                public BigDecimal getAmount() {
                    return (BigDecimal) get("amount");
                }

                public Payment setAmount(BigDecimal arg) {
                    optimisticData.put(getKey("amount"), arg);
                    return this;
                }

                /**
                * The billing address for the payment.
                */

                public MailingAddress getBillingAddress() {
                    return (MailingAddress) get("billingAddress");
                }

                public Payment setBillingAddress(MailingAddress arg) {
                    optimisticData.put(getKey("billingAddress"), arg);
                    return this;
                }

                /**
                * The checkout to which the payment belongs.
                */

                public Checkout getCheckout() {
                    return (Checkout) get("checkout");
                }

                public Payment setCheckout(Checkout arg) {
                    optimisticData.put(getKey("checkout"), arg);
                    return this;
                }

                /**
                * The credit card used for the payment in the case of direct payments.
                */

                public CreditCard getCreditCard() {
                    return (CreditCard) get("creditCard");
                }

                public Payment setCreditCard(CreditCard arg) {
                    optimisticData.put(getKey("creditCard"), arg);
                    return this;
                }

                /**
                * An message describing a processing error during asynchronous processing.
                */

                public String getErrorMessage() {
                    return (String) get("errorMessage");
                }

                public Payment setErrorMessage(String arg) {
                    optimisticData.put(getKey("errorMessage"), arg);
                    return this;
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                /**
                * A client-side generated token to identify a payment and perform idempotent operations.
                */

                public String getIdempotencyKey() {
                    return (String) get("idempotencyKey");
                }

                public Payment setIdempotencyKey(String arg) {
                    optimisticData.put(getKey("idempotencyKey"), arg);
                    return this;
                }

                /**
                * Whether or not the payment is still processing asynchronously.
                */

                public Boolean getReady() {
                    return (Boolean) get("ready");
                }

                public Payment setReady(Boolean arg) {
                    optimisticData.put(getKey("ready"), arg);
                    return this;
                }

                /**
                * A flag to indicate if the payment is to be done in test mode for gateways that support it.
                */

                public Boolean getTest() {
                    return (Boolean) get("test");
                }

                public Payment setTest(Boolean arg) {
                    optimisticData.put(getKey("test"), arg);
                    return this;
                }

                /**
                * The actual transaction recorded by Shopify after having processed the payment with the gateway.
                */

                public Transaction getTransaction() {
                    return (Transaction) get("transaction");
                }

                public Payment setTransaction(Transaction arg) {
                    optimisticData.put(getKey("transaction"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "amount": return false;

                        case "billingAddress": return true;

                        case "checkout": return true;

                        case "creditCard": return true;

                        case "errorMessage": return false;

                        case "id": return false;

                        case "idempotencyKey": return false;

                        case "ready": return false;

                        case "test": return false;

                        case "transaction": return true;

                        default: return false;
                    }
                }
            }

            public interface PaymentSettingsQueryDefinition {
                void define(PaymentSettingsQuery _queryBuilder);
            }

            /**
            * Settings related to payments.
            */
            public static class PaymentSettingsQuery extends Query<PaymentSettingsQuery> {
                PaymentSettingsQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * List of the card brands which the shop accepts.
                */
                public PaymentSettingsQuery acceptedCardBrands() {
                    startField("acceptedCardBrands");

                    return this;
                }

                /**
                * The url pointing to the endpoint to vault credit cards.
                */
                public PaymentSettingsQuery cardVaultUrl() {
                    startField("cardVaultUrl");

                    return this;
                }

                /**
                * The country where the shop is located.
                */
                public PaymentSettingsQuery countryCode() {
                    startField("countryCode");

                    return this;
                }

                /**
                * The three-letter code for the currency that the shop accepts.
                */
                public PaymentSettingsQuery currencyCode() {
                    startField("currencyCode");

                    return this;
                }

                /**
                * The shop’s Shopify Payments account id.
                */
                public PaymentSettingsQuery shopifyPaymentsAccountId() {
                    startField("shopifyPaymentsAccountId");

                    return this;
                }

                /**
                * List of the digital wallets which the shop supports.
                */
                public PaymentSettingsQuery supportedDigitalWallets() {
                    startField("supportedDigitalWallets");

                    return this;
                }
            }

            /**
            * Settings related to payments.
            */
            public static class PaymentSettings extends AbstractResponse<PaymentSettings> {
                public PaymentSettings() {
                }

                public PaymentSettings(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "acceptedCardBrands": {
                                List<CardBrand> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(CardBrand.fromGraphQl(jsonAsString(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "cardVaultUrl": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "countryCode": {
                                responseData.put(key, CountryCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "currencyCode": {
                                responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "shopifyPaymentsAccountId": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "supportedDigitalWallets": {
                                List<DigitalWallet> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(DigitalWallet.fromGraphQl(jsonAsString(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "PaymentSettings";
                }

                /**
                * List of the card brands which the shop accepts.
                */

                public List<CardBrand> getAcceptedCardBrands() {
                    return (List<CardBrand>) get("acceptedCardBrands");
                }

                public PaymentSettings setAcceptedCardBrands(List<CardBrand> arg) {
                    optimisticData.put(getKey("acceptedCardBrands"), arg);
                    return this;
                }

                /**
                * The url pointing to the endpoint to vault credit cards.
                */

                public String getCardVaultUrl() {
                    return (String) get("cardVaultUrl");
                }

                public PaymentSettings setCardVaultUrl(String arg) {
                    optimisticData.put(getKey("cardVaultUrl"), arg);
                    return this;
                }

                /**
                * The country where the shop is located.
                */

                public CountryCode getCountryCode() {
                    return (CountryCode) get("countryCode");
                }

                public PaymentSettings setCountryCode(CountryCode arg) {
                    optimisticData.put(getKey("countryCode"), arg);
                    return this;
                }

                /**
                * The three-letter code for the currency that the shop accepts.
                */

                public CurrencyCode getCurrencyCode() {
                    return (CurrencyCode) get("currencyCode");
                }

                public PaymentSettings setCurrencyCode(CurrencyCode arg) {
                    optimisticData.put(getKey("currencyCode"), arg);
                    return this;
                }

                /**
                * The shop’s Shopify Payments account id.
                */

                public String getShopifyPaymentsAccountId() {
                    return (String) get("shopifyPaymentsAccountId");
                }

                public PaymentSettings setShopifyPaymentsAccountId(String arg) {
                    optimisticData.put(getKey("shopifyPaymentsAccountId"), arg);
                    return this;
                }

                /**
                * List of the digital wallets which the shop supports.
                */

                public List<DigitalWallet> getSupportedDigitalWallets() {
                    return (List<DigitalWallet>) get("supportedDigitalWallets");
                }

                public PaymentSettings setSupportedDigitalWallets(List<DigitalWallet> arg) {
                    optimisticData.put(getKey("supportedDigitalWallets"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "acceptedCardBrands": return false;

                        case "cardVaultUrl": return false;

                        case "countryCode": return false;

                        case "currencyCode": return false;

                        case "shopifyPaymentsAccountId": return false;

                        case "supportedDigitalWallets": return false;

                        default: return false;
                    }
                }
            }

            public interface ProductQueryDefinition {
                void define(ProductQuery _queryBuilder);
            }

            /**
            * A product represents an individual item for sale in a Shopify store. Products are often physical,
            * but they don't have to be.
            * For example, a digital download (such as a movie, music or ebook file) also qualifies as a product,
            * as do services (such as equipment rental, work for hire, customization of another product or an
            * extended warranty).
            */
            public static class ProductQuery extends Query<ProductQuery> {
                ProductQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("id");
                }

                /**
                * Indicates if at least one product variant is available for sale.
                */
                public ProductQuery availableForSale() {
                    startField("availableForSale");

                    return this;
                }

                public class CollectionsArguments extends Arguments {
                    CollectionsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public CollectionsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public CollectionsArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public CollectionsArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public CollectionsArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public CollectionsArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface CollectionsArgumentsDefinition {
                    void define(CollectionsArguments args);
                }

                /**
                * List of collections a product belongs to.
                */
                public ProductQuery collections(CollectionConnectionQueryDefinition queryDef) {
                    return collections(args -> {}, queryDef);
                }

                /**
                * List of collections a product belongs to.
                */
                public ProductQuery collections(CollectionsArgumentsDefinition argsDef, CollectionConnectionQueryDefinition queryDef) {
                    startField("collections");

                    CollectionsArguments args = new CollectionsArguments(_queryBuilder);
                    argsDef.define(args);
                    CollectionsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new CollectionConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The date and time when the product was created.
                */
                public ProductQuery createdAt() {
                    startField("createdAt");

                    return this;
                }

                public class DescriptionArguments extends Arguments {
                    DescriptionArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Truncates string after the given length.
                    */
                    public DescriptionArguments truncateAt(Integer value) {
                        if (value != null) {
                            startArgument("truncateAt");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface DescriptionArgumentsDefinition {
                    void define(DescriptionArguments args);
                }

                /**
                * Stripped description of the product, single line with HTML tags removed.
                */
                public ProductQuery description() {
                    return description(args -> {});
                }

                /**
                * Stripped description of the product, single line with HTML tags removed.
                */
                public ProductQuery description(DescriptionArgumentsDefinition argsDef) {
                    startField("description");

                    DescriptionArguments args = new DescriptionArguments(_queryBuilder);
                    argsDef.define(args);
                    DescriptionArguments.end(args);

                    return this;
                }

                /**
                * The description of the product, complete with HTML formatting.
                */
                public ProductQuery descriptionHtml() {
                    startField("descriptionHtml");

                    return this;
                }

                /**
                * A human-friendly unique string for the Product automatically generated from its title.
                * They are used by the Liquid templating language to refer to objects.
                */
                public ProductQuery handle() {
                    startField("handle");

                    return this;
                }

                public class ImagesArguments extends Arguments {
                    ImagesArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public ImagesArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public ImagesArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public ImagesArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public ImagesArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public ImagesArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Sort the underlying list by the given key.
                    */
                    public ImagesArguments sortKey(ProductImageSortKeys value) {
                        if (value != null) {
                            startArgument("sortKey");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Image width in pixels between 1 and 2048. This argument is deprecated: Use `maxWidth` on
                    * `Image.transformedSrc` instead.
                    */
                    public ImagesArguments maxWidth(Integer value) {
                        if (value != null) {
                            startArgument("maxWidth");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Image height in pixels between 1 and 2048. This argument is deprecated: Use `maxHeight` on
                    * `Image.transformedSrc` instead.
                    */
                    public ImagesArguments maxHeight(Integer value) {
                        if (value != null) {
                            startArgument("maxHeight");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Crops the image according to the specified region. This argument is deprecated: Use `crop` on
                    * `Image.transformedSrc` instead.
                    */
                    public ImagesArguments crop(CropRegion value) {
                        if (value != null) {
                            startArgument("crop");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Image size multiplier for high-resolution retina displays. Must be between 1 and 3. This argument is
                    * deprecated: Use `scale` on `Image.transformedSrc` instead.
                    */
                    public ImagesArguments scale(Integer value) {
                        if (value != null) {
                            startArgument("scale");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface ImagesArgumentsDefinition {
                    void define(ImagesArguments args);
                }

                /**
                * List of images associated with the product.
                */
                public ProductQuery images(ImageConnectionQueryDefinition queryDef) {
                    return images(args -> {}, queryDef);
                }

                /**
                * List of images associated with the product.
                */
                public ProductQuery images(ImagesArgumentsDefinition argsDef, ImageConnectionQueryDefinition queryDef) {
                    startField("images");

                    ImagesArguments args = new ImagesArguments(_queryBuilder);
                    argsDef.define(args);
                    ImagesArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new ImageConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The online store URL for the product.
                * A value of `null` indicates that the product is not published to the Online Store sales channel.
                */
                public ProductQuery onlineStoreUrl() {
                    startField("onlineStoreUrl");

                    return this;
                }

                public class OptionsArguments extends Arguments {
                    OptionsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Truncate the array result to this size.
                    */
                    public OptionsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface OptionsArgumentsDefinition {
                    void define(OptionsArguments args);
                }

                /**
                * List of custom product options (maximum of 3 per product).
                */
                public ProductQuery options(ProductOptionQueryDefinition queryDef) {
                    return options(args -> {}, queryDef);
                }

                /**
                * List of custom product options (maximum of 3 per product).
                */
                public ProductQuery options(OptionsArgumentsDefinition argsDef, ProductOptionQueryDefinition queryDef) {
                    startField("options");

                    OptionsArguments args = new OptionsArguments(_queryBuilder);
                    argsDef.define(args);
                    OptionsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new ProductOptionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The price range.
                */
                public ProductQuery priceRange(ProductPriceRangeQueryDefinition queryDef) {
                    startField("priceRange");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductPriceRangeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * A categorization that a product can be tagged with, commonly used for filtering and searching.
                */
                public ProductQuery productType() {
                    startField("productType");

                    return this;
                }

                /**
                * The date and time when the product was published to the channel.
                */
                public ProductQuery publishedAt() {
                    startField("publishedAt");

                    return this;
                }

                /**
                * A categorization that a product can be tagged with, commonly used for filtering and searching.
                * Each comma-separated tag has a character limit of 255.
                */
                public ProductQuery tags() {
                    startField("tags");

                    return this;
                }

                /**
                * The product’s title.
                */
                public ProductQuery title() {
                    startField("title");

                    return this;
                }

                /**
                * The date and time when the product was last modified.
                */
                public ProductQuery updatedAt() {
                    startField("updatedAt");

                    return this;
                }

                /**
                * Find a product’s variant based on its selected options.
                * This is useful for converting a user’s selection of product options into a single matching variant.
                * If there is not a variant for the selected options, `null` will be returned.
                */
                public ProductQuery variantBySelectedOptions(List<SelectedOptionInput> selectedOptions, ProductVariantQueryDefinition queryDef) {
                    startField("variantBySelectedOptions");

                    _queryBuilder.append("(selectedOptions:");
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (SelectedOptionInput item1 : selectedOptions) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            item1.appendTo(_queryBuilder);
                        }
                    }
                    _queryBuilder.append(']');

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new ProductVariantQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public class VariantsArguments extends Arguments {
                    VariantsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public VariantsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public VariantsArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public VariantsArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public VariantsArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public VariantsArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Sort the underlying list by the given key.
                    */
                    public VariantsArguments sortKey(ProductVariantSortKeys value) {
                        if (value != null) {
                            startArgument("sortKey");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }
                }

                public interface VariantsArgumentsDefinition {
                    void define(VariantsArguments args);
                }

                /**
                * List of the product’s variants.
                */
                public ProductQuery variants(ProductVariantConnectionQueryDefinition queryDef) {
                    return variants(args -> {}, queryDef);
                }

                /**
                * List of the product’s variants.
                */
                public ProductQuery variants(VariantsArgumentsDefinition argsDef, ProductVariantConnectionQueryDefinition queryDef) {
                    startField("variants");

                    VariantsArguments args = new VariantsArguments(_queryBuilder);
                    argsDef.define(args);
                    VariantsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new ProductVariantConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The product’s vendor name.
                */
                public ProductQuery vendor() {
                    startField("vendor");

                    return this;
                }
            }

            /**
            * A product represents an individual item for sale in a Shopify store. Products are often physical,
            * but they don't have to be.
            * For example, a digital download (such as a movie, music or ebook file) also qualifies as a product,
            * as do services (such as equipment rental, work for hire, customization of another product or an
            * extended warranty).
            */
            public static class Product extends AbstractResponse<Product> implements Node {
                public Product() {
                }

                public Product(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "availableForSale": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "collections": {
                                responseData.put(key, new CollectionConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "createdAt": {
                                responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "description": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "descriptionHtml": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "handle": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "images": {
                                responseData.put(key, new ImageConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "onlineStoreUrl": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "options": {
                                List<ProductOption> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new ProductOption(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "priceRange": {
                                responseData.put(key, new ProductPriceRange(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "productType": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "publishedAt": {
                                responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "tags": {
                                List<String> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(jsonAsString(element1, key));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "title": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "updatedAt": {
                                responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "variantBySelectedOptions": {
                                ProductVariant optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new ProductVariant(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "variants": {
                                responseData.put(key, new ProductVariantConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "vendor": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public Product(ID id) {
                    this();
                    optimisticData.put("id", id);
                }

                public String getGraphQlTypeName() {
                    return "Product";
                }

                /**
                * Indicates if at least one product variant is available for sale.
                */

                public Boolean getAvailableForSale() {
                    return (Boolean) get("availableForSale");
                }

                public Product setAvailableForSale(Boolean arg) {
                    optimisticData.put(getKey("availableForSale"), arg);
                    return this;
                }

                /**
                * List of collections a product belongs to.
                */

                public CollectionConnection getCollections() {
                    return (CollectionConnection) get("collections");
                }

                public Product setCollections(CollectionConnection arg) {
                    optimisticData.put(getKey("collections"), arg);
                    return this;
                }

                /**
                * The date and time when the product was created.
                */

                public DateTime getCreatedAt() {
                    return (DateTime) get("createdAt");
                }

                public Product setCreatedAt(DateTime arg) {
                    optimisticData.put(getKey("createdAt"), arg);
                    return this;
                }

                /**
                * Stripped description of the product, single line with HTML tags removed.
                */

                public String getDescription() {
                    return (String) get("description");
                }

                public Product setDescription(String arg) {
                    optimisticData.put(getKey("description"), arg);
                    return this;
                }

                /**
                * The description of the product, complete with HTML formatting.
                */

                public String getDescriptionHtml() {
                    return (String) get("descriptionHtml");
                }

                public Product setDescriptionHtml(String arg) {
                    optimisticData.put(getKey("descriptionHtml"), arg);
                    return this;
                }

                /**
                * A human-friendly unique string for the Product automatically generated from its title.
                * They are used by the Liquid templating language to refer to objects.
                */

                public String getHandle() {
                    return (String) get("handle");
                }

                public Product setHandle(String arg) {
                    optimisticData.put(getKey("handle"), arg);
                    return this;
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                /**
                * List of images associated with the product.
                */

                public ImageConnection getImages() {
                    return (ImageConnection) get("images");
                }

                public Product setImages(ImageConnection arg) {
                    optimisticData.put(getKey("images"), arg);
                    return this;
                }

                /**
                * The online store URL for the product.
                * A value of `null` indicates that the product is not published to the Online Store sales channel.
                */

                public String getOnlineStoreUrl() {
                    return (String) get("onlineStoreUrl");
                }

                public Product setOnlineStoreUrl(String arg) {
                    optimisticData.put(getKey("onlineStoreUrl"), arg);
                    return this;
                }

                /**
                * List of custom product options (maximum of 3 per product).
                */

                public List<ProductOption> getOptions() {
                    return (List<ProductOption>) get("options");
                }

                public Product setOptions(List<ProductOption> arg) {
                    optimisticData.put(getKey("options"), arg);
                    return this;
                }

                /**
                * The price range.
                */

                public ProductPriceRange getPriceRange() {
                    return (ProductPriceRange) get("priceRange");
                }

                public Product setPriceRange(ProductPriceRange arg) {
                    optimisticData.put(getKey("priceRange"), arg);
                    return this;
                }

                /**
                * A categorization that a product can be tagged with, commonly used for filtering and searching.
                */

                public String getProductType() {
                    return (String) get("productType");
                }

                public Product setProductType(String arg) {
                    optimisticData.put(getKey("productType"), arg);
                    return this;
                }

                /**
                * The date and time when the product was published to the channel.
                */

                public DateTime getPublishedAt() {
                    return (DateTime) get("publishedAt");
                }

                public Product setPublishedAt(DateTime arg) {
                    optimisticData.put(getKey("publishedAt"), arg);
                    return this;
                }

                /**
                * A categorization that a product can be tagged with, commonly used for filtering and searching.
                * Each comma-separated tag has a character limit of 255.
                */

                public List<String> getTags() {
                    return (List<String>) get("tags");
                }

                public Product setTags(List<String> arg) {
                    optimisticData.put(getKey("tags"), arg);
                    return this;
                }

                /**
                * The product’s title.
                */

                public String getTitle() {
                    return (String) get("title");
                }

                public Product setTitle(String arg) {
                    optimisticData.put(getKey("title"), arg);
                    return this;
                }

                /**
                * The date and time when the product was last modified.
                */

                public DateTime getUpdatedAt() {
                    return (DateTime) get("updatedAt");
                }

                public Product setUpdatedAt(DateTime arg) {
                    optimisticData.put(getKey("updatedAt"), arg);
                    return this;
                }

                /**
                * Find a product’s variant based on its selected options.
                * This is useful for converting a user’s selection of product options into a single matching variant.
                * If there is not a variant for the selected options, `null` will be returned.
                */

                public ProductVariant getVariantBySelectedOptions() {
                    return (ProductVariant) get("variantBySelectedOptions");
                }

                public Product setVariantBySelectedOptions(ProductVariant arg) {
                    optimisticData.put(getKey("variantBySelectedOptions"), arg);
                    return this;
                }

                /**
                * List of the product’s variants.
                */

                public ProductVariantConnection getVariants() {
                    return (ProductVariantConnection) get("variants");
                }

                public Product setVariants(ProductVariantConnection arg) {
                    optimisticData.put(getKey("variants"), arg);
                    return this;
                }

                /**
                * The product’s vendor name.
                */

                public String getVendor() {
                    return (String) get("vendor");
                }

                public Product setVendor(String arg) {
                    optimisticData.put(getKey("vendor"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "availableForSale": return false;

                        case "collections": return true;

                        case "createdAt": return false;

                        case "description": return false;

                        case "descriptionHtml": return false;

                        case "handle": return false;

                        case "id": return false;

                        case "images": return true;

                        case "onlineStoreUrl": return false;

                        case "options": return true;

                        case "priceRange": return true;

                        case "productType": return false;

                        case "publishedAt": return false;

                        case "tags": return false;

                        case "title": return false;

                        case "updatedAt": return false;

                        case "variantBySelectedOptions": return true;

                        case "variants": return true;

                        case "vendor": return false;

                        default: return false;
                    }
                }
            }

            /**
            * The set of valid sort keys for the products query.
            */
            public enum ProductCollectionSortKeys {
                BEST_SELLING,

                COLLECTION_DEFAULT,

                CREATED,

                ID,

                MANUAL,

                PRICE,

                /**
                * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
                * the
                * results by relevance to the search term(s). When no search query is specified, this sort key is not
                * deterministic and should not be used.
                */
                RELEVANCE,

                TITLE,

                UNKNOWN_VALUE;

                public static ProductCollectionSortKeys fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "BEST_SELLING": {
                            return BEST_SELLING;
                        }

                        case "COLLECTION_DEFAULT": {
                            return COLLECTION_DEFAULT;
                        }

                        case "CREATED": {
                            return CREATED;
                        }

                        case "ID": {
                            return ID;
                        }

                        case "MANUAL": {
                            return MANUAL;
                        }

                        case "PRICE": {
                            return PRICE;
                        }

                        case "RELEVANCE": {
                            return RELEVANCE;
                        }

                        case "TITLE": {
                            return TITLE;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case BEST_SELLING: {
                            return "BEST_SELLING";
                        }

                        case COLLECTION_DEFAULT: {
                            return "COLLECTION_DEFAULT";
                        }

                        case CREATED: {
                            return "CREATED";
                        }

                        case ID: {
                            return "ID";
                        }

                        case MANUAL: {
                            return "MANUAL";
                        }

                        case PRICE: {
                            return "PRICE";
                        }

                        case RELEVANCE: {
                            return "RELEVANCE";
                        }

                        case TITLE: {
                            return "TITLE";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public interface ProductConnectionQueryDefinition {
                void define(ProductConnectionQuery _queryBuilder);
            }

            public static class ProductConnectionQuery extends Query<ProductConnectionQuery> {
                ProductConnectionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A list of edges.
                */
                public ProductConnectionQuery edges(ProductEdgeQueryDefinition queryDef) {
                    startField("edges");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductEdgeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Information to aid in pagination.
                */
                public ProductConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
                    startField("pageInfo");

                    _queryBuilder.append('{');
                    queryDef.define(new PageInfoQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class ProductConnection extends AbstractResponse<ProductConnection> {
                public ProductConnection() {
                }

                public ProductConnection(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "edges": {
                                List<ProductEdge> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new ProductEdge(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "pageInfo": {
                                responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "ProductConnection";
                }

                /**
                * A list of edges.
                */

                public List<ProductEdge> getEdges() {
                    return (List<ProductEdge>) get("edges");
                }

                public ProductConnection setEdges(List<ProductEdge> arg) {
                    optimisticData.put(getKey("edges"), arg);
                    return this;
                }

                /**
                * Information to aid in pagination.
                */

                public PageInfo getPageInfo() {
                    return (PageInfo) get("pageInfo");
                }

                public ProductConnection setPageInfo(PageInfo arg) {
                    optimisticData.put(getKey("pageInfo"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "edges": return true;

                        case "pageInfo": return true;

                        default: return false;
                    }
                }
            }

            public interface ProductEdgeQueryDefinition {
                void define(ProductEdgeQuery _queryBuilder);
            }

            public static class ProductEdgeQuery extends Query<ProductEdgeQuery> {
                ProductEdgeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A cursor for use in pagination.
                */
                public ProductEdgeQuery cursor() {
                    startField("cursor");

                    return this;
                }

                /**
                * The item at the end of ProductEdge.
                */
                public ProductEdgeQuery node(ProductQueryDefinition queryDef) {
                    startField("node");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class ProductEdge extends AbstractResponse<ProductEdge> {
                public ProductEdge() {
                }

                public ProductEdge(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "cursor": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "node": {
                                responseData.put(key, new Product(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "ProductEdge";
                }

                /**
                * A cursor for use in pagination.
                */

                public String getCursor() {
                    return (String) get("cursor");
                }

                public ProductEdge setCursor(String arg) {
                    optimisticData.put(getKey("cursor"), arg);
                    return this;
                }

                /**
                * The item at the end of ProductEdge.
                */

                public Product getNode() {
                    return (Product) get("node");
                }

                public ProductEdge setNode(Product arg) {
                    optimisticData.put(getKey("node"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "cursor": return false;

                        case "node": return true;

                        default: return false;
                    }
                }
            }

            /**
            * The set of valid sort keys for the images query.
            */
            public enum ProductImageSortKeys {
                CREATED_AT,

                ID,

                POSITION,

                /**
                * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
                * the
                * results by relevance to the search term(s). When no search query is specified, this sort key is not
                * deterministic and should not be used.
                */
                RELEVANCE,

                UNKNOWN_VALUE;

                public static ProductImageSortKeys fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "CREATED_AT": {
                            return CREATED_AT;
                        }

                        case "ID": {
                            return ID;
                        }

                        case "POSITION": {
                            return POSITION;
                        }

                        case "RELEVANCE": {
                            return RELEVANCE;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case CREATED_AT: {
                            return "CREATED_AT";
                        }

                        case ID: {
                            return "ID";
                        }

                        case POSITION: {
                            return "POSITION";
                        }

                        case RELEVANCE: {
                            return "RELEVANCE";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public interface ProductOptionQueryDefinition {
                void define(ProductOptionQuery _queryBuilder);
            }

            /**
            * Custom product property names like "Size", "Color", and "Material".
            * Products are based on permutations of these options.
            * A product may have a maximum of 3 options.
            * 255 characters limit each.
            */
            public static class ProductOptionQuery extends Query<ProductOptionQuery> {
                ProductOptionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("id");
                }

                /**
                * The product option’s name.
                */
                public ProductOptionQuery name() {
                    startField("name");

                    return this;
                }

                /**
                * The corresponding value to the product option name.
                */
                public ProductOptionQuery values() {
                    startField("values");

                    return this;
                }
            }

            /**
            * Custom product property names like "Size", "Color", and "Material".
            * Products are based on permutations of these options.
            * A product may have a maximum of 3 options.
            * 255 characters limit each.
            */
            public static class ProductOption extends AbstractResponse<ProductOption> implements Node {
                public ProductOption() {
                }

                public ProductOption(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "name": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "values": {
                                List<String> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(jsonAsString(element1, key));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public ProductOption(ID id) {
                    this();
                    optimisticData.put("id", id);
                }

                public String getGraphQlTypeName() {
                    return "ProductOption";
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                /**
                * The product option’s name.
                */

                public String getName() {
                    return (String) get("name");
                }

                public ProductOption setName(String arg) {
                    optimisticData.put(getKey("name"), arg);
                    return this;
                }

                /**
                * The corresponding value to the product option name.
                */

                public List<String> getValues() {
                    return (List<String>) get("values");
                }

                public ProductOption setValues(List<String> arg) {
                    optimisticData.put(getKey("values"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "id": return false;

                        case "name": return false;

                        case "values": return false;

                        default: return false;
                    }
                }
            }

            public interface ProductPriceRangeQueryDefinition {
                void define(ProductPriceRangeQuery _queryBuilder);
            }

            /**
            * The price range of the product.
            */
            public static class ProductPriceRangeQuery extends Query<ProductPriceRangeQuery> {
                ProductPriceRangeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * The highest variant's price.
                */
                public ProductPriceRangeQuery maxVariantPrice(MoneyV2QueryDefinition queryDef) {
                    startField("maxVariantPrice");

                    _queryBuilder.append('{');
                    queryDef.define(new MoneyV2Query(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The lowest variant's price.
                */
                public ProductPriceRangeQuery minVariantPrice(MoneyV2QueryDefinition queryDef) {
                    startField("minVariantPrice");

                    _queryBuilder.append('{');
                    queryDef.define(new MoneyV2Query(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            /**
            * The price range of the product.
            */
            public static class ProductPriceRange extends AbstractResponse<ProductPriceRange> {
                public ProductPriceRange() {
                }

                public ProductPriceRange(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "maxVariantPrice": {
                                responseData.put(key, new MoneyV2(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "minVariantPrice": {
                                responseData.put(key, new MoneyV2(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "ProductPriceRange";
                }

                /**
                * The highest variant's price.
                */

                public MoneyV2 getMaxVariantPrice() {
                    return (MoneyV2) get("maxVariantPrice");
                }

                public ProductPriceRange setMaxVariantPrice(MoneyV2 arg) {
                    optimisticData.put(getKey("maxVariantPrice"), arg);
                    return this;
                }

                /**
                * The lowest variant's price.
                */

                public MoneyV2 getMinVariantPrice() {
                    return (MoneyV2) get("minVariantPrice");
                }

                public ProductPriceRange setMinVariantPrice(MoneyV2 arg) {
                    optimisticData.put(getKey("minVariantPrice"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "maxVariantPrice": return true;

                        case "minVariantPrice": return true;

                        default: return false;
                    }
                }
            }

            /**
            * The set of valid sort keys for the products query.
            */
            public enum ProductSortKeys {
                CREATED_AT,

                ID,

                PRODUCT_TYPE,

                /**
                * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
                * the
                * results by relevance to the search term(s). When no search query is specified, this sort key is not
                * deterministic and should not be used.
                */
                RELEVANCE,

                TITLE,

                UPDATED_AT,

                VENDOR,

                UNKNOWN_VALUE;

                public static ProductSortKeys fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "CREATED_AT": {
                            return CREATED_AT;
                        }

                        case "ID": {
                            return ID;
                        }

                        case "PRODUCT_TYPE": {
                            return PRODUCT_TYPE;
                        }

                        case "RELEVANCE": {
                            return RELEVANCE;
                        }

                        case "TITLE": {
                            return TITLE;
                        }

                        case "UPDATED_AT": {
                            return UPDATED_AT;
                        }

                        case "VENDOR": {
                            return VENDOR;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case CREATED_AT: {
                            return "CREATED_AT";
                        }

                        case ID: {
                            return "ID";
                        }

                        case PRODUCT_TYPE: {
                            return "PRODUCT_TYPE";
                        }

                        case RELEVANCE: {
                            return "RELEVANCE";
                        }

                        case TITLE: {
                            return "TITLE";
                        }

                        case UPDATED_AT: {
                            return "UPDATED_AT";
                        }

                        case VENDOR: {
                            return "VENDOR";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public interface ProductVariantQueryDefinition {
                void define(ProductVariantQuery _queryBuilder);
            }

            /**
            * A product variant represents a different version of a product, such as differing sizes or differing
            * colors.
            */
            public static class ProductVariantQuery extends Query<ProductVariantQuery> {
                ProductVariantQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("id");
                }

                /**
                * Indicates if the product variant is in stock.
                *
                * @deprecated Use `availableForSale` instead
                */
                @Deprecated
                public ProductVariantQuery available() {
                    startField("available");

                    return this;
                }

                /**
                * Indicates if the product variant is available for sale.
                */
                public ProductVariantQuery availableForSale() {
                    startField("availableForSale");

                    return this;
                }

                /**
                * The compare at price of the variant. This can be used to mark a variant as on sale, when
                * `compareAtPrice` is higher than `price`.
                */
                public ProductVariantQuery compareAtPrice() {
                    startField("compareAtPrice");

                    return this;
                }

                public class ImageArguments extends Arguments {
                    ImageArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Image width in pixels between 1 and 2048. This argument is deprecated: Use `maxWidth` on
                    * `Image.transformedSrc` instead.
                    */
                    public ImageArguments maxWidth(Integer value) {
                        if (value != null) {
                            startArgument("maxWidth");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Image height in pixels between 1 and 2048. This argument is deprecated: Use `maxHeight` on
                    * `Image.transformedSrc` instead.
                    */
                    public ImageArguments maxHeight(Integer value) {
                        if (value != null) {
                            startArgument("maxHeight");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Crops the image according to the specified region. This argument is deprecated: Use `crop` on
                    * `Image.transformedSrc` instead.
                    */
                    public ImageArguments crop(CropRegion value) {
                        if (value != null) {
                            startArgument("crop");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Image size multiplier for high-resolution retina displays. Must be between 1 and 3. This argument is
                    * deprecated: Use `scale` on `Image.transformedSrc` instead.
                    */
                    public ImageArguments scale(Integer value) {
                        if (value != null) {
                            startArgument("scale");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }
                }

                public interface ImageArgumentsDefinition {
                    void define(ImageArguments args);
                }

                /**
                * Image associated with the product variant. This field falls back to the product image if no image is
                * available.
                */
                public ProductVariantQuery image(ImageQueryDefinition queryDef) {
                    return image(args -> {}, queryDef);
                }

                /**
                * Image associated with the product variant. This field falls back to the product image if no image is
                * available.
                */
                public ProductVariantQuery image(ImageArgumentsDefinition argsDef, ImageQueryDefinition queryDef) {
                    startField("image");

                    ImageArguments args = new ImageArguments(_queryBuilder);
                    argsDef.define(args);
                    ImageArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new ImageQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The product variant’s price.
                */
                public ProductVariantQuery price() {
                    startField("price");

                    return this;
                }

                /**
                * The product object that the product variant belongs to.
                */
                public ProductVariantQuery product(ProductQueryDefinition queryDef) {
                    startField("product");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * List of product options applied to the variant.
                */
                public ProductVariantQuery selectedOptions(SelectedOptionQueryDefinition queryDef) {
                    startField("selectedOptions");

                    _queryBuilder.append('{');
                    queryDef.define(new SelectedOptionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The SKU (Stock Keeping Unit) associated with the variant.
                */
                public ProductVariantQuery sku() {
                    startField("sku");

                    return this;
                }

                /**
                * The product variant’s title.
                */
                public ProductVariantQuery title() {
                    startField("title");

                    return this;
                }

                /**
                * The weight of the product variant in the unit system specified with `weight_unit`.
                */
                public ProductVariantQuery weight() {
                    startField("weight");

                    return this;
                }

                /**
                * Unit of measurement for weight.
                */
                public ProductVariantQuery weightUnit() {
                    startField("weightUnit");

                    return this;
                }
            }

            /**
            * A product variant represents a different version of a product, such as differing sizes or differing
            * colors.
            */
            public static class ProductVariant extends AbstractResponse<ProductVariant> implements Node {
                public ProductVariant() {
                }

                public ProductVariant(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "available": {
                                Boolean optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsBoolean(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "availableForSale": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "compareAtPrice": {
                                BigDecimal optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new BigDecimal(jsonAsString(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "image": {
                                Image optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new Image(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "price": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "product": {
                                responseData.put(key, new Product(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "selectedOptions": {
                                List<SelectedOption> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new SelectedOption(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "sku": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "title": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "weight": {
                                Double optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsDouble(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "weightUnit": {
                                responseData.put(key, WeightUnit.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public ProductVariant(ID id) {
                    this();
                    optimisticData.put("id", id);
                }

                public String getGraphQlTypeName() {
                    return "ProductVariant";
                }

                /**
                * Indicates if the product variant is in stock.
                *
                * @deprecated Use `availableForSale` instead
                */

                public Boolean getAvailable() {
                    return (Boolean) get("available");
                }

                public ProductVariant setAvailable(Boolean arg) {
                    optimisticData.put(getKey("available"), arg);
                    return this;
                }

                /**
                * Indicates if the product variant is available for sale.
                */

                public Boolean getAvailableForSale() {
                    return (Boolean) get("availableForSale");
                }

                public ProductVariant setAvailableForSale(Boolean arg) {
                    optimisticData.put(getKey("availableForSale"), arg);
                    return this;
                }

                /**
                * The compare at price of the variant. This can be used to mark a variant as on sale, when
                * `compareAtPrice` is higher than `price`.
                */

                public BigDecimal getCompareAtPrice() {
                    return (BigDecimal) get("compareAtPrice");
                }

                public ProductVariant setCompareAtPrice(BigDecimal arg) {
                    optimisticData.put(getKey("compareAtPrice"), arg);
                    return this;
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                /**
                * Image associated with the product variant. This field falls back to the product image if no image is
                * available.
                */

                public Image getImage() {
                    return (Image) get("image");
                }

                public ProductVariant setImage(Image arg) {
                    optimisticData.put(getKey("image"), arg);
                    return this;
                }

                /**
                * The product variant’s price.
                */

                public BigDecimal getPrice() {
                    return (BigDecimal) get("price");
                }

                public ProductVariant setPrice(BigDecimal arg) {
                    optimisticData.put(getKey("price"), arg);
                    return this;
                }

                /**
                * The product object that the product variant belongs to.
                */

                public Product getProduct() {
                    return (Product) get("product");
                }

                public ProductVariant setProduct(Product arg) {
                    optimisticData.put(getKey("product"), arg);
                    return this;
                }

                /**
                * List of product options applied to the variant.
                */

                public List<SelectedOption> getSelectedOptions() {
                    return (List<SelectedOption>) get("selectedOptions");
                }

                public ProductVariant setSelectedOptions(List<SelectedOption> arg) {
                    optimisticData.put(getKey("selectedOptions"), arg);
                    return this;
                }

                /**
                * The SKU (Stock Keeping Unit) associated with the variant.
                */

                public String getSku() {
                    return (String) get("sku");
                }

                public ProductVariant setSku(String arg) {
                    optimisticData.put(getKey("sku"), arg);
                    return this;
                }

                /**
                * The product variant’s title.
                */

                public String getTitle() {
                    return (String) get("title");
                }

                public ProductVariant setTitle(String arg) {
                    optimisticData.put(getKey("title"), arg);
                    return this;
                }

                /**
                * The weight of the product variant in the unit system specified with `weight_unit`.
                */

                public Double getWeight() {
                    return (Double) get("weight");
                }

                public ProductVariant setWeight(Double arg) {
                    optimisticData.put(getKey("weight"), arg);
                    return this;
                }

                /**
                * Unit of measurement for weight.
                */

                public WeightUnit getWeightUnit() {
                    return (WeightUnit) get("weightUnit");
                }

                public ProductVariant setWeightUnit(WeightUnit arg) {
                    optimisticData.put(getKey("weightUnit"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "available": return false;

                        case "availableForSale": return false;

                        case "compareAtPrice": return false;

                        case "id": return false;

                        case "image": return true;

                        case "price": return false;

                        case "product": return true;

                        case "selectedOptions": return true;

                        case "sku": return false;

                        case "title": return false;

                        case "weight": return false;

                        case "weightUnit": return false;

                        default: return false;
                    }
                }
            }

            public interface ProductVariantConnectionQueryDefinition {
                void define(ProductVariantConnectionQuery _queryBuilder);
            }

            public static class ProductVariantConnectionQuery extends Query<ProductVariantConnectionQuery> {
                ProductVariantConnectionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A list of edges.
                */
                public ProductVariantConnectionQuery edges(ProductVariantEdgeQueryDefinition queryDef) {
                    startField("edges");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductVariantEdgeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Information to aid in pagination.
                */
                public ProductVariantConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
                    startField("pageInfo");

                    _queryBuilder.append('{');
                    queryDef.define(new PageInfoQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class ProductVariantConnection extends AbstractResponse<ProductVariantConnection> {
                public ProductVariantConnection() {
                }

                public ProductVariantConnection(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "edges": {
                                List<ProductVariantEdge> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new ProductVariantEdge(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "pageInfo": {
                                responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "ProductVariantConnection";
                }

                /**
                * A list of edges.
                */

                public List<ProductVariantEdge> getEdges() {
                    return (List<ProductVariantEdge>) get("edges");
                }

                public ProductVariantConnection setEdges(List<ProductVariantEdge> arg) {
                    optimisticData.put(getKey("edges"), arg);
                    return this;
                }

                /**
                * Information to aid in pagination.
                */

                public PageInfo getPageInfo() {
                    return (PageInfo) get("pageInfo");
                }

                public ProductVariantConnection setPageInfo(PageInfo arg) {
                    optimisticData.put(getKey("pageInfo"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "edges": return true;

                        case "pageInfo": return true;

                        default: return false;
                    }
                }
            }

            public interface ProductVariantEdgeQueryDefinition {
                void define(ProductVariantEdgeQuery _queryBuilder);
            }

            public static class ProductVariantEdgeQuery extends Query<ProductVariantEdgeQuery> {
                ProductVariantEdgeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A cursor for use in pagination.
                */
                public ProductVariantEdgeQuery cursor() {
                    startField("cursor");

                    return this;
                }

                /**
                * The item at the end of ProductVariantEdge.
                */
                public ProductVariantEdgeQuery node(ProductVariantQueryDefinition queryDef) {
                    startField("node");

                    _queryBuilder.append('{');
                    queryDef.define(new ProductVariantQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class ProductVariantEdge extends AbstractResponse<ProductVariantEdge> {
                public ProductVariantEdge() {
                }

                public ProductVariantEdge(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "cursor": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "node": {
                                responseData.put(key, new ProductVariant(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "ProductVariantEdge";
                }

                /**
                * A cursor for use in pagination.
                */

                public String getCursor() {
                    return (String) get("cursor");
                }

                public ProductVariantEdge setCursor(String arg) {
                    optimisticData.put(getKey("cursor"), arg);
                    return this;
                }

                /**
                * The item at the end of ProductVariantEdge.
                */

                public ProductVariant getNode() {
                    return (ProductVariant) get("node");
                }

                public ProductVariantEdge setNode(ProductVariant arg) {
                    optimisticData.put(getKey("node"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "cursor": return false;

                        case "node": return true;

                        default: return false;
                    }
                }
            }

            /**
            * The set of valid sort keys for the variants query.
            */
            public enum ProductVariantSortKeys {
                ID,

                POSITION,

                /**
                * During a search (i.e. when the `query` parameter has been specified on the connection) this sorts
                * the
                * results by relevance to the search term(s). When no search query is specified, this sort key is not
                * deterministic and should not be used.
                */
                RELEVANCE,

                SKU,

                TITLE,

                UNKNOWN_VALUE;

                public static ProductVariantSortKeys fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "ID": {
                            return ID;
                        }

                        case "POSITION": {
                            return POSITION;
                        }

                        case "RELEVANCE": {
                            return RELEVANCE;
                        }

                        case "SKU": {
                            return SKU;
                        }

                        case "TITLE": {
                            return TITLE;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case ID: {
                            return "ID";
                        }

                        case POSITION: {
                            return "POSITION";
                        }

                        case RELEVANCE: {
                            return "RELEVANCE";
                        }

                        case SKU: {
                            return "SKU";
                        }

                        case TITLE: {
                            return "TITLE";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public interface QueryRootQueryDefinition {
                void define(QueryRootQuery _queryBuilder);
            }

            /**
            * The schema’s entry-point for queries. This acts as the public, top-level API from which all queries
            * must start.
            */
            public static class QueryRootQuery extends Query<QueryRootQuery> {
                QueryRootQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                public QueryRootQuery customer(String customerAccessToken, CustomerQueryDefinition queryDef) {
                    startField("customer");

                    _queryBuilder.append("(customerAccessToken:");
                    Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CustomerQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public QueryRootQuery node(ID id, NodeQueryDefinition queryDef) {
                    startField("node");

                    _queryBuilder.append("(id:");
                    Query.appendQuotedString(_queryBuilder, id.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new NodeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public QueryRootQuery nodes(List<ID> ids, NodeQueryDefinition queryDef) {
                    startField("nodes");

                    _queryBuilder.append("(ids:");
                    _queryBuilder.append('[');
                    {
                        String listSeperator1 = "";
                        for (ID item1 : ids) {
                            _queryBuilder.append(listSeperator1);
                            listSeperator1 = ",";
                            Query.appendQuotedString(_queryBuilder, item1.toString());
                        }
                    }
                    _queryBuilder.append(']');

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new NodeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public QueryRootQuery shop(ShopQueryDefinition queryDef) {
                    startField("shop");

                    _queryBuilder.append('{');
                    queryDef.define(new ShopQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public String toString() {
                    return _queryBuilder.toString();
                }
            }

            /**
            * The schema’s entry-point for queries. This acts as the public, top-level API from which all queries
            * must start.
            */
            public static class QueryRoot extends AbstractResponse<QueryRoot> {
                public QueryRoot() {
                }

                public QueryRoot(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "customer": {
                                Customer optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new Customer(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "node": {
                                Node optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = UnknownNode.create(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "nodes": {
                                List<Node> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    Node optional2 = null;
                                    if (!element1.isJsonNull()) {
                                        optional2 = UnknownNode.create(jsonAsObject(element1, key));
                                    }

                                    list1.add(optional2);
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "shop": {
                                responseData.put(key, new Shop(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "QueryRoot";
                }

                public Customer getCustomer() {
                    return (Customer) get("customer");
                }

                public QueryRoot setCustomer(Customer arg) {
                    optimisticData.put(getKey("customer"), arg);
                    return this;
                }

                public Node getNode() {
                    return (Node) get("node");
                }

                public QueryRoot setNode(Node arg) {
                    optimisticData.put(getKey("node"), arg);
                    return this;
                }

                public List<Node> getNodes() {
                    return (List<Node>) get("nodes");
                }

                public QueryRoot setNodes(List<Node> arg) {
                    optimisticData.put(getKey("nodes"), arg);
                    return this;
                }

                public Shop getShop() {
                    return (Shop) get("shop");
                }

                public QueryRoot setShop(Shop arg) {
                    optimisticData.put(getKey("shop"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "customer": return true;

                        case "node": return false;

                        case "nodes": return false;

                        case "shop": return true;

                        default: return false;
                    }
                }
            }

            public interface SelectedOptionQueryDefinition {
                void define(SelectedOptionQuery _queryBuilder);
            }

            /**
            * Custom properties that a shop owner can use to define product variants.
            * Multiple options can exist. Options are represented as: option1, option2, option3, etc.
            */
            public static class SelectedOptionQuery extends Query<SelectedOptionQuery> {
                SelectedOptionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * The product option’s name.
                */
                public SelectedOptionQuery name() {
                    startField("name");

                    return this;
                }

                /**
                * The product option’s value.
                */
                public SelectedOptionQuery value() {
                    startField("value");

                    return this;
                }
            }

            /**
            * Custom properties that a shop owner can use to define product variants.
            * Multiple options can exist. Options are represented as: option1, option2, option3, etc.
            */
            public static class SelectedOption extends AbstractResponse<SelectedOption> {
                public SelectedOption() {
                }

                public SelectedOption(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "name": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "value": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "SelectedOption";
                }

                /**
                * The product option’s name.
                */

                public String getName() {
                    return (String) get("name");
                }

                public SelectedOption setName(String arg) {
                    optimisticData.put(getKey("name"), arg);
                    return this;
                }

                /**
                * The product option’s value.
                */

                public String getValue() {
                    return (String) get("value");
                }

                public SelectedOption setValue(String arg) {
                    optimisticData.put(getKey("value"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "name": return false;

                        case "value": return false;

                        default: return false;
                    }
                }
            }

            public static class SelectedOptionInput implements Serializable {
                private String name;

                private String value;

                public SelectedOptionInput(String name, String value) {
                    this.name = name;

                    this.value = value;
                }

                public String getName() {
                    return name;
                }

                public SelectedOptionInput setName(String name) {
                    this.name = name;
                    return this;
                }

                public String getValue() {
                    return value;
                }

                public SelectedOptionInput setValue(String value) {
                    this.value = value;
                    return this;
                }

                public void appendTo(StringBuilder _queryBuilder) {
                    String separator = "";
                    _queryBuilder.append('{');

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("name:");
                    Query.appendQuotedString(_queryBuilder, name.toString());

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("value:");
                    Query.appendQuotedString(_queryBuilder, value.toString());

                    _queryBuilder.append('}');
                }
            }

            public interface ShippingRateQueryDefinition {
                void define(ShippingRateQuery _queryBuilder);
            }

            /**
            * A shipping rate to be applied to a checkout.
            */
            public static class ShippingRateQuery extends Query<ShippingRateQuery> {
                ShippingRateQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * Human-readable unique identifier for this shipping rate.
                */
                public ShippingRateQuery handle() {
                    startField("handle");

                    return this;
                }

                /**
                * Price of this shipping rate.
                */
                public ShippingRateQuery price() {
                    startField("price");

                    return this;
                }

                /**
                * Title of this shipping rate.
                */
                public ShippingRateQuery title() {
                    startField("title");

                    return this;
                }
            }

            /**
            * A shipping rate to be applied to a checkout.
            */
            public static class ShippingRate extends AbstractResponse<ShippingRate> {
                public ShippingRate() {
                }

                public ShippingRate(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "handle": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "price": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "title": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "ShippingRate";
                }

                /**
                * Human-readable unique identifier for this shipping rate.
                */

                public String getHandle() {
                    return (String) get("handle");
                }

                public ShippingRate setHandle(String arg) {
                    optimisticData.put(getKey("handle"), arg);
                    return this;
                }

                /**
                * Price of this shipping rate.
                */

                public BigDecimal getPrice() {
                    return (BigDecimal) get("price");
                }

                public ShippingRate setPrice(BigDecimal arg) {
                    optimisticData.put(getKey("price"), arg);
                    return this;
                }

                /**
                * Title of this shipping rate.
                */

                public String getTitle() {
                    return (String) get("title");
                }

                public ShippingRate setTitle(String arg) {
                    optimisticData.put(getKey("title"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "handle": return false;

                        case "price": return false;

                        case "title": return false;

                        default: return false;
                    }
                }
            }

            public interface ShopQueryDefinition {
                void define(ShopQuery _queryBuilder);
            }

            /**
            * Shop represents a collection of the general settings and information about the shop.
            */
            public static class ShopQuery extends Query<ShopQuery> {
                ShopQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                public class ArticlesArguments extends Arguments {
                    ArticlesArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public ArticlesArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public ArticlesArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public ArticlesArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public ArticlesArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public ArticlesArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Sort the underlying list by the given key.
                    */
                    public ArticlesArguments sortKey(ArticleSortKeys value) {
                        if (value != null) {
                            startArgument("sortKey");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Supported filter parameters:
                    * - `author`
                    * - `updated_at`
                    * - `created_at`
                    * - `blog_title`
                    * - `tag`
                    */
                    public ArticlesArguments query(String value) {
                        if (value != null) {
                            startArgument("query");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }
                }

                public interface ArticlesArgumentsDefinition {
                    void define(ArticlesArguments args);
                }

                /**
                * List of the shop' articles.
                */
                public ShopQuery articles(ArticleConnectionQueryDefinition queryDef) {
                    return articles(args -> {}, queryDef);
                }

                /**
                * List of the shop' articles.
                */
                public ShopQuery articles(ArticlesArgumentsDefinition argsDef, ArticleConnectionQueryDefinition queryDef) {
                    startField("articles");

                    ArticlesArguments args = new ArticlesArguments(_queryBuilder);
                    argsDef.define(args);
                    ArticlesArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new ArticleConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public class BlogsArguments extends Arguments {
                    BlogsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public BlogsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public BlogsArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public BlogsArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public BlogsArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public BlogsArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Sort the underlying list by the given key.
                    */
                    public BlogsArguments sortKey(BlogSortKeys value) {
                        if (value != null) {
                            startArgument("sortKey");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Supported filter parameters:
                    * - `handle`
                    * - `title`
                    * - `updated_at`
                    * - `created_at`
                    */
                    public BlogsArguments query(String value) {
                        if (value != null) {
                            startArgument("query");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }
                }

                public interface BlogsArgumentsDefinition {
                    void define(BlogsArguments args);
                }

                /**
                * List of the shop' blogs.
                */
                public ShopQuery blogs(BlogConnectionQueryDefinition queryDef) {
                    return blogs(args -> {}, queryDef);
                }

                /**
                * List of the shop' blogs.
                */
                public ShopQuery blogs(BlogsArgumentsDefinition argsDef, BlogConnectionQueryDefinition queryDef) {
                    startField("blogs");

                    BlogsArguments args = new BlogsArguments(_queryBuilder);
                    argsDef.define(args);
                    BlogsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new BlogConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The url pointing to the endpoint to vault credit cards.
                *
                * @deprecated Use `paymentSettings` instead
                */
                @Deprecated
                public ShopQuery cardVaultUrl() {
                    startField("cardVaultUrl");

                    return this;
                }

                /**
                * Find a collection by its handle.
                */
                public ShopQuery collectionByHandle(String handle, CollectionQueryDefinition queryDef) {
                    startField("collectionByHandle");

                    _queryBuilder.append("(handle:");
                    Query.appendQuotedString(_queryBuilder, handle.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new CollectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public class CollectionsArguments extends Arguments {
                    CollectionsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public CollectionsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public CollectionsArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public CollectionsArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public CollectionsArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public CollectionsArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Sort the underlying list by the given key.
                    */
                    public CollectionsArguments sortKey(CollectionSortKeys value) {
                        if (value != null) {
                            startArgument("sortKey");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Supported filter parameters:
                    * - `title`
                    * - `collection_type`
                    * - `updated_at`
                    */
                    public CollectionsArguments query(String value) {
                        if (value != null) {
                            startArgument("query");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }
                }

                public interface CollectionsArgumentsDefinition {
                    void define(CollectionsArguments args);
                }

                /**
                * List of the shop’s collections.
                */
                public ShopQuery collections(CollectionConnectionQueryDefinition queryDef) {
                    return collections(args -> {}, queryDef);
                }

                /**
                * List of the shop’s collections.
                */
                public ShopQuery collections(CollectionsArgumentsDefinition argsDef, CollectionConnectionQueryDefinition queryDef) {
                    startField("collections");

                    CollectionsArguments args = new CollectionsArguments(_queryBuilder);
                    argsDef.define(args);
                    CollectionsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new CollectionConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The three-letter code for the currency that the shop accepts.
                *
                * @deprecated Use `paymentSettings` instead
                */
                @Deprecated
                public ShopQuery currencyCode() {
                    startField("currencyCode");

                    return this;
                }

                /**
                * A description of the shop.
                */
                public ShopQuery description() {
                    startField("description");

                    return this;
                }

                /**
                * A string representing the way currency is formatted when the currency isn’t specified.
                */
                public ShopQuery moneyFormat() {
                    startField("moneyFormat");

                    return this;
                }

                /**
                * The shop’s name.
                */
                public ShopQuery name() {
                    startField("name");

                    return this;
                }

                /**
                * Settings related to payments.
                */
                public ShopQuery paymentSettings(PaymentSettingsQueryDefinition queryDef) {
                    startField("paymentSettings");

                    _queryBuilder.append('{');
                    queryDef.define(new PaymentSettingsQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The shop’s primary domain.
                */
                public ShopQuery primaryDomain(DomainQueryDefinition queryDef) {
                    startField("primaryDomain");

                    _queryBuilder.append('{');
                    queryDef.define(new DomainQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The shop’s privacy policy.
                */
                public ShopQuery privacyPolicy(ShopPolicyQueryDefinition queryDef) {
                    startField("privacyPolicy");

                    _queryBuilder.append('{');
                    queryDef.define(new ShopPolicyQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Find a product by its handle.
                */
                public ShopQuery productByHandle(String handle, ProductQueryDefinition queryDef) {
                    startField("productByHandle");

                    _queryBuilder.append("(handle:");
                    Query.appendQuotedString(_queryBuilder, handle.toString());

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new ProductQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * List of the shop’s product types.
                */
                public ShopQuery productTypes(int first, StringConnectionQueryDefinition queryDef) {
                    startField("productTypes");

                    _queryBuilder.append("(first:");
                    _queryBuilder.append(first);

                    _queryBuilder.append(')');

                    _queryBuilder.append('{');
                    queryDef.define(new StringConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                public class ProductsArguments extends Arguments {
                    ProductsArguments(StringBuilder _queryBuilder) {
                        super(_queryBuilder, true);
                    }

                    /**
                    * Returns up to the first `n` elements from the list.
                    */
                    public ProductsArguments first(Integer value) {
                        if (value != null) {
                            startArgument("first");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come after the specified cursor.
                    */
                    public ProductsArguments after(String value) {
                        if (value != null) {
                            startArgument("after");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Returns up to the last `n` elements from the list.
                    */
                    public ProductsArguments last(Integer value) {
                        if (value != null) {
                            startArgument("last");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Returns the elements that come before the specified cursor.
                    */
                    public ProductsArguments before(String value) {
                        if (value != null) {
                            startArgument("before");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }

                    /**
                    * Reverse the order of the underlying list.
                    */
                    public ProductsArguments reverse(Boolean value) {
                        if (value != null) {
                            startArgument("reverse");
                            _queryBuilder.append(value);
                        }
                        return this;
                    }

                    /**
                    * Sort the underlying list by the given key.
                    */
                    public ProductsArguments sortKey(ProductSortKeys value) {
                        if (value != null) {
                            startArgument("sortKey");
                            _queryBuilder.append(value.toString());
                        }
                        return this;
                    }

                    /**
                    * Supported filter parameters:
                    * - `title`
                    * - `product_type`
                    * - `vendor`
                    * - `created_at`
                    * - `updated_at`
                    * - `tag`
                    */
                    public ProductsArguments query(String value) {
                        if (value != null) {
                            startArgument("query");
                            Query.appendQuotedString(_queryBuilder, value.toString());
                        }
                        return this;
                    }
                }

                public interface ProductsArgumentsDefinition {
                    void define(ProductsArguments args);
                }

                /**
                * List of the shop’s products.
                */
                public ShopQuery products(ProductConnectionQueryDefinition queryDef) {
                    return products(args -> {}, queryDef);
                }

                /**
                * List of the shop’s products.
                */
                public ShopQuery products(ProductsArgumentsDefinition argsDef, ProductConnectionQueryDefinition queryDef) {
                    startField("products");

                    ProductsArguments args = new ProductsArguments(_queryBuilder);
                    argsDef.define(args);
                    ProductsArguments.end(args);

                    _queryBuilder.append('{');
                    queryDef.define(new ProductConnectionQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * The shop’s refund policy.
                */
                public ShopQuery refundPolicy(ShopPolicyQueryDefinition queryDef) {
                    startField("refundPolicy");

                    _queryBuilder.append('{');
                    queryDef.define(new ShopPolicyQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Countries that the shop ships to.
                */
                public ShopQuery shipsToCountries() {
                    startField("shipsToCountries");

                    return this;
                }

                /**
                * The shop’s Shopify Payments account id.
                *
                * @deprecated Use `paymentSettings` instead
                */
                @Deprecated
                public ShopQuery shopifyPaymentsAccountId() {
                    startField("shopifyPaymentsAccountId");

                    return this;
                }

                /**
                * The shop’s terms of service.
                */
                public ShopQuery termsOfService(ShopPolicyQueryDefinition queryDef) {
                    startField("termsOfService");

                    _queryBuilder.append('{');
                    queryDef.define(new ShopPolicyQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            /**
            * Shop represents a collection of the general settings and information about the shop.
            */
            public static class Shop extends AbstractResponse<Shop> {
                public Shop() {
                }

                public Shop(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "articles": {
                                responseData.put(key, new ArticleConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "blogs": {
                                responseData.put(key, new BlogConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "cardVaultUrl": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "collectionByHandle": {
                                Collection optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new Collection(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "collections": {
                                responseData.put(key, new CollectionConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "currencyCode": {
                                responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "description": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "moneyFormat": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "name": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "paymentSettings": {
                                responseData.put(key, new PaymentSettings(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "primaryDomain": {
                                responseData.put(key, new Domain(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "privacyPolicy": {
                                ShopPolicy optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new ShopPolicy(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "productByHandle": {
                                Product optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new Product(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "productTypes": {
                                responseData.put(key, new StringConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "products": {
                                responseData.put(key, new ProductConnection(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "refundPolicy": {
                                ShopPolicy optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new ShopPolicy(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "shipsToCountries": {
                                List<CountryCode> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(CountryCode.fromGraphQl(jsonAsString(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "shopifyPaymentsAccountId": {
                                String optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = jsonAsString(field.getValue(), key);
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "termsOfService": {
                                ShopPolicy optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    optional1 = new ShopPolicy(jsonAsObject(field.getValue(), key));
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "Shop";
                }

                /**
                * List of the shop' articles.
                */

                public ArticleConnection getArticles() {
                    return (ArticleConnection) get("articles");
                }

                public Shop setArticles(ArticleConnection arg) {
                    optimisticData.put(getKey("articles"), arg);
                    return this;
                }

                /**
                * List of the shop' blogs.
                */

                public BlogConnection getBlogs() {
                    return (BlogConnection) get("blogs");
                }

                public Shop setBlogs(BlogConnection arg) {
                    optimisticData.put(getKey("blogs"), arg);
                    return this;
                }

                /**
                * The url pointing to the endpoint to vault credit cards.
                *
                * @deprecated Use `paymentSettings` instead
                */

                public String getCardVaultUrl() {
                    return (String) get("cardVaultUrl");
                }

                public Shop setCardVaultUrl(String arg) {
                    optimisticData.put(getKey("cardVaultUrl"), arg);
                    return this;
                }

                /**
                * Find a collection by its handle.
                */

                public Collection getCollectionByHandle() {
                    return (Collection) get("collectionByHandle");
                }

                public Shop setCollectionByHandle(Collection arg) {
                    optimisticData.put(getKey("collectionByHandle"), arg);
                    return this;
                }

                /**
                * List of the shop’s collections.
                */

                public CollectionConnection getCollections() {
                    return (CollectionConnection) get("collections");
                }

                public Shop setCollections(CollectionConnection arg) {
                    optimisticData.put(getKey("collections"), arg);
                    return this;
                }

                /**
                * The three-letter code for the currency that the shop accepts.
                *
                * @deprecated Use `paymentSettings` instead
                */

                public CurrencyCode getCurrencyCode() {
                    return (CurrencyCode) get("currencyCode");
                }

                public Shop setCurrencyCode(CurrencyCode arg) {
                    optimisticData.put(getKey("currencyCode"), arg);
                    return this;
                }

                /**
                * A description of the shop.
                */

                public String getDescription() {
                    return (String) get("description");
                }

                public Shop setDescription(String arg) {
                    optimisticData.put(getKey("description"), arg);
                    return this;
                }

                /**
                * A string representing the way currency is formatted when the currency isn’t specified.
                */

                public String getMoneyFormat() {
                    return (String) get("moneyFormat");
                }

                public Shop setMoneyFormat(String arg) {
                    optimisticData.put(getKey("moneyFormat"), arg);
                    return this;
                }

                /**
                * The shop’s name.
                */

                public String getName() {
                    return (String) get("name");
                }

                public Shop setName(String arg) {
                    optimisticData.put(getKey("name"), arg);
                    return this;
                }

                /**
                * Settings related to payments.
                */

                public PaymentSettings getPaymentSettings() {
                    return (PaymentSettings) get("paymentSettings");
                }

                public Shop setPaymentSettings(PaymentSettings arg) {
                    optimisticData.put(getKey("paymentSettings"), arg);
                    return this;
                }

                /**
                * The shop’s primary domain.
                */

                public Domain getPrimaryDomain() {
                    return (Domain) get("primaryDomain");
                }

                public Shop setPrimaryDomain(Domain arg) {
                    optimisticData.put(getKey("primaryDomain"), arg);
                    return this;
                }

                /**
                * The shop’s privacy policy.
                */

                public ShopPolicy getPrivacyPolicy() {
                    return (ShopPolicy) get("privacyPolicy");
                }

                public Shop setPrivacyPolicy(ShopPolicy arg) {
                    optimisticData.put(getKey("privacyPolicy"), arg);
                    return this;
                }

                /**
                * Find a product by its handle.
                */

                public Product getProductByHandle() {
                    return (Product) get("productByHandle");
                }

                public Shop setProductByHandle(Product arg) {
                    optimisticData.put(getKey("productByHandle"), arg);
                    return this;
                }

                /**
                * List of the shop’s product types.
                */

                public StringConnection getProductTypes() {
                    return (StringConnection) get("productTypes");
                }

                public Shop setProductTypes(StringConnection arg) {
                    optimisticData.put(getKey("productTypes"), arg);
                    return this;
                }

                /**
                * List of the shop’s products.
                */

                public ProductConnection getProducts() {
                    return (ProductConnection) get("products");
                }

                public Shop setProducts(ProductConnection arg) {
                    optimisticData.put(getKey("products"), arg);
                    return this;
                }

                /**
                * The shop’s refund policy.
                */

                public ShopPolicy getRefundPolicy() {
                    return (ShopPolicy) get("refundPolicy");
                }

                public Shop setRefundPolicy(ShopPolicy arg) {
                    optimisticData.put(getKey("refundPolicy"), arg);
                    return this;
                }

                /**
                * Countries that the shop ships to.
                */

                public List<CountryCode> getShipsToCountries() {
                    return (List<CountryCode>) get("shipsToCountries");
                }

                public Shop setShipsToCountries(List<CountryCode> arg) {
                    optimisticData.put(getKey("shipsToCountries"), arg);
                    return this;
                }

                /**
                * The shop’s Shopify Payments account id.
                *
                * @deprecated Use `paymentSettings` instead
                */

                public String getShopifyPaymentsAccountId() {
                    return (String) get("shopifyPaymentsAccountId");
                }

                public Shop setShopifyPaymentsAccountId(String arg) {
                    optimisticData.put(getKey("shopifyPaymentsAccountId"), arg);
                    return this;
                }

                /**
                * The shop’s terms of service.
                */

                public ShopPolicy getTermsOfService() {
                    return (ShopPolicy) get("termsOfService");
                }

                public Shop setTermsOfService(ShopPolicy arg) {
                    optimisticData.put(getKey("termsOfService"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "articles": return true;

                        case "blogs": return true;

                        case "cardVaultUrl": return false;

                        case "collectionByHandle": return true;

                        case "collections": return true;

                        case "currencyCode": return false;

                        case "description": return false;

                        case "moneyFormat": return false;

                        case "name": return false;

                        case "paymentSettings": return true;

                        case "primaryDomain": return true;

                        case "privacyPolicy": return true;

                        case "productByHandle": return true;

                        case "productTypes": return true;

                        case "products": return true;

                        case "refundPolicy": return true;

                        case "shipsToCountries": return false;

                        case "shopifyPaymentsAccountId": return false;

                        case "termsOfService": return true;

                        default: return false;
                    }
                }
            }

            public interface ShopPolicyQueryDefinition {
                void define(ShopPolicyQuery _queryBuilder);
            }

            /**
            * Policy that a merchant has configured for their store, such as their refund or privacy policy.
            */
            public static class ShopPolicyQuery extends Query<ShopPolicyQuery> {
                ShopPolicyQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);

                    startField("id");
                }

                /**
                * Policy text, maximum size of 64kb.
                */
                public ShopPolicyQuery body() {
                    startField("body");

                    return this;
                }

                /**
                * Policy’s title.
                */
                public ShopPolicyQuery title() {
                    startField("title");

                    return this;
                }

                /**
                * Public URL to the policy.
                */
                public ShopPolicyQuery url() {
                    startField("url");

                    return this;
                }
            }

            /**
            * Policy that a merchant has configured for their store, such as their refund or privacy policy.
            */
            public static class ShopPolicy extends AbstractResponse<ShopPolicy> implements Node {
                public ShopPolicy() {
                }

                public ShopPolicy(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "body": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "id": {
                                responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "title": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "url": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public ShopPolicy(ID id) {
                    this();
                    optimisticData.put("id", id);
                }

                public String getGraphQlTypeName() {
                    return "ShopPolicy";
                }

                /**
                * Policy text, maximum size of 64kb.
                */

                public String getBody() {
                    return (String) get("body");
                }

                public ShopPolicy setBody(String arg) {
                    optimisticData.put(getKey("body"), arg);
                    return this;
                }

                /**
                * Globally unique identifier.
                */

                public ID getId() {
                    return (ID) get("id");
                }

                /**
                * Policy’s title.
                */

                public String getTitle() {
                    return (String) get("title");
                }

                public ShopPolicy setTitle(String arg) {
                    optimisticData.put(getKey("title"), arg);
                    return this;
                }

                /**
                * Public URL to the policy.
                */

                public String getUrl() {
                    return (String) get("url");
                }

                public ShopPolicy setUrl(String arg) {
                    optimisticData.put(getKey("url"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "body": return false;

                        case "id": return false;

                        case "title": return false;

                        case "url": return false;

                        default: return false;
                    }
                }
            }

            public interface StringConnectionQueryDefinition {
                void define(StringConnectionQuery _queryBuilder);
            }

            public static class StringConnectionQuery extends Query<StringConnectionQuery> {
                StringConnectionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A list of edges.
                */
                public StringConnectionQuery edges(StringEdgeQueryDefinition queryDef) {
                    startField("edges");

                    _queryBuilder.append('{');
                    queryDef.define(new StringEdgeQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }

                /**
                * Information to aid in pagination.
                */
                public StringConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
                    startField("pageInfo");

                    _queryBuilder.append('{');
                    queryDef.define(new PageInfoQuery(_queryBuilder));
                    _queryBuilder.append('}');

                    return this;
                }
            }

            public static class StringConnection extends AbstractResponse<StringConnection> {
                public StringConnection() {
                }

                public StringConnection(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "edges": {
                                List<StringEdge> list1 = new ArrayList<>();
                                for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                    list1.add(new StringEdge(jsonAsObject(element1, key)));
                                }

                                responseData.put(key, list1);

                                break;
                            }

                            case "pageInfo": {
                                responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "StringConnection";
                }

                /**
                * A list of edges.
                */

                public List<StringEdge> getEdges() {
                    return (List<StringEdge>) get("edges");
                }

                public StringConnection setEdges(List<StringEdge> arg) {
                    optimisticData.put(getKey("edges"), arg);
                    return this;
                }

                /**
                * Information to aid in pagination.
                */

                public PageInfo getPageInfo() {
                    return (PageInfo) get("pageInfo");
                }

                public StringConnection setPageInfo(PageInfo arg) {
                    optimisticData.put(getKey("pageInfo"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "edges": return true;

                        case "pageInfo": return true;

                        default: return false;
                    }
                }
            }

            public interface StringEdgeQueryDefinition {
                void define(StringEdgeQuery _queryBuilder);
            }

            public static class StringEdgeQuery extends Query<StringEdgeQuery> {
                StringEdgeQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * A cursor for use in pagination.
                */
                public StringEdgeQuery cursor() {
                    startField("cursor");

                    return this;
                }

                /**
                * The item at the end of StringEdge.
                */
                public StringEdgeQuery node() {
                    startField("node");

                    return this;
                }
            }

            public static class StringEdge extends AbstractResponse<StringEdge> {
                public StringEdge() {
                }

                public StringEdge(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "cursor": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "node": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "StringEdge";
                }

                /**
                * A cursor for use in pagination.
                */

                public String getCursor() {
                    return (String) get("cursor");
                }

                public StringEdge setCursor(String arg) {
                    optimisticData.put(getKey("cursor"), arg);
                    return this;
                }

                /**
                * The item at the end of StringEdge.
                */

                public String getNode() {
                    return (String) get("node");
                }

                public StringEdge setNode(String arg) {
                    optimisticData.put(getKey("node"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "cursor": return false;

                        case "node": return false;

                        default: return false;
                    }
                }
            }

            public static class TokenizedPaymentInput implements Serializable {
                private BigDecimal amount;

                private String idempotencyKey;

                private MailingAddressInput billingAddress;

                private String type;

                private String paymentData;

                private Input<Boolean> test = Input.undefined();

                private Input<String> identifier = Input.undefined();

                public TokenizedPaymentInput(BigDecimal amount, String idempotencyKey, MailingAddressInput billingAddress, String type, String paymentData) {
                    this.amount = amount;

                    this.idempotencyKey = idempotencyKey;

                    this.billingAddress = billingAddress;

                    this.type = type;

                    this.paymentData = paymentData;
                }

                public BigDecimal getAmount() {
                    return amount;
                }

                public TokenizedPaymentInput setAmount(BigDecimal amount) {
                    this.amount = amount;
                    return this;
                }

                public String getIdempotencyKey() {
                    return idempotencyKey;
                }

                public TokenizedPaymentInput setIdempotencyKey(String idempotencyKey) {
                    this.idempotencyKey = idempotencyKey;
                    return this;
                }

                public MailingAddressInput getBillingAddress() {
                    return billingAddress;
                }

                public TokenizedPaymentInput setBillingAddress(MailingAddressInput billingAddress) {
                    this.billingAddress = billingAddress;
                    return this;
                }

                public String getType() {
                    return type;
                }

                public TokenizedPaymentInput setType(String type) {
                    this.type = type;
                    return this;
                }

                public String getPaymentData() {
                    return paymentData;
                }

                public TokenizedPaymentInput setPaymentData(String paymentData) {
                    this.paymentData = paymentData;
                    return this;
                }

                public Boolean getTest() {
                    return test.getValue();
                }

                public Input<Boolean> getTestInput() {
                    return test;
                }

                public TokenizedPaymentInput setTest(Boolean test) {
                    this.test = Input.optional(test);
                    return this;
                }

                public TokenizedPaymentInput setTestInput(Input<Boolean> test) {
                    if (test == null) {
                        throw new IllegalArgumentException("Input can not be null");
                    }
                    this.test = test;
                    return this;
                }

                public String getIdentifier() {
                    return identifier.getValue();
                }

                public Input<String> getIdentifierInput() {
                    return identifier;
                }

                public TokenizedPaymentInput setIdentifier(String identifier) {
                    this.identifier = Input.optional(identifier);
                    return this;
                }

                public TokenizedPaymentInput setIdentifierInput(Input<String> identifier) {
                    if (identifier == null) {
                        throw new IllegalArgumentException("Input can not be null");
                    }
                    this.identifier = identifier;
                    return this;
                }

                public void appendTo(StringBuilder _queryBuilder) {
                    String separator = "";
                    _queryBuilder.append('{');

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("amount:");
                    Query.appendQuotedString(_queryBuilder, amount.toString());

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("idempotencyKey:");
                    Query.appendQuotedString(_queryBuilder, idempotencyKey.toString());

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("billingAddress:");
                    billingAddress.appendTo(_queryBuilder);

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("type:");
                    Query.appendQuotedString(_queryBuilder, type.toString());

                    _queryBuilder.append(separator);
                    separator = ",";
                    _queryBuilder.append("paymentData:");
                    Query.appendQuotedString(_queryBuilder, paymentData.toString());

                    if (this.test.isDefined()) {
                        _queryBuilder.append(separator);
                        separator = ",";
                        _queryBuilder.append("test:");
                        if (test.getValue() != null) {
                            _queryBuilder.append(test.getValue());
                        } else {
                            _queryBuilder.append("null");
                        }
                    }

                    if (this.identifier.isDefined()) {
                        _queryBuilder.append(separator);
                        separator = ",";
                        _queryBuilder.append("identifier:");
                        if (identifier.getValue() != null) {
                            Query.appendQuotedString(_queryBuilder, identifier.getValue().toString());
                        } else {
                            _queryBuilder.append("null");
                        }
                    }

                    _queryBuilder.append('}');
                }
            }

            public interface TransactionQueryDefinition {
                void define(TransactionQuery _queryBuilder);
            }

            /**
            * An object representing exchange of money for a product or service.
            */
            public static class TransactionQuery extends Query<TransactionQuery> {
                TransactionQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * The amount of money that the transaction was for.
                */
                public TransactionQuery amount() {
                    startField("amount");

                    return this;
                }

                /**
                * The kind of the transaction.
                */
                public TransactionQuery kind() {
                    startField("kind");

                    return this;
                }

                /**
                * The status of the transaction
                */
                public TransactionQuery status() {
                    startField("status");

                    return this;
                }

                /**
                * Whether the transaction was done in test mode or not
                */
                public TransactionQuery test() {
                    startField("test");

                    return this;
                }
            }

            /**
            * An object representing exchange of money for a product or service.
            */
            public static class Transaction extends AbstractResponse<Transaction> {
                public Transaction() {
                }

                public Transaction(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "amount": {
                                responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "kind": {
                                responseData.put(key, TransactionKind.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "status": {
                                responseData.put(key, TransactionStatus.fromGraphQl(jsonAsString(field.getValue(), key)));

                                break;
                            }

                            case "test": {
                                responseData.put(key, jsonAsBoolean(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "Transaction";
                }

                /**
                * The amount of money that the transaction was for.
                */

                public BigDecimal getAmount() {
                    return (BigDecimal) get("amount");
                }

                public Transaction setAmount(BigDecimal arg) {
                    optimisticData.put(getKey("amount"), arg);
                    return this;
                }

                /**
                * The kind of the transaction.
                */

                public TransactionKind getKind() {
                    return (TransactionKind) get("kind");
                }

                public Transaction setKind(TransactionKind arg) {
                    optimisticData.put(getKey("kind"), arg);
                    return this;
                }

                /**
                * The status of the transaction
                */

                public TransactionStatus getStatus() {
                    return (TransactionStatus) get("status");
                }

                public Transaction setStatus(TransactionStatus arg) {
                    optimisticData.put(getKey("status"), arg);
                    return this;
                }

                /**
                * Whether the transaction was done in test mode or not
                */

                public Boolean getTest() {
                    return (Boolean) get("test");
                }

                public Transaction setTest(Boolean arg) {
                    optimisticData.put(getKey("test"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "amount": return false;

                        case "kind": return false;

                        case "status": return false;

                        case "test": return false;

                        default: return false;
                    }
                }
            }

            public enum TransactionKind {
                AUTHORIZATION,

                CAPTURE,

                CHANGE,

                EMV_AUTHORIZATION,

                SALE,

                UNKNOWN_VALUE;

                public static TransactionKind fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "AUTHORIZATION": {
                            return AUTHORIZATION;
                        }

                        case "CAPTURE": {
                            return CAPTURE;
                        }

                        case "CHANGE": {
                            return CHANGE;
                        }

                        case "EMV_AUTHORIZATION": {
                            return EMV_AUTHORIZATION;
                        }

                        case "SALE": {
                            return SALE;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case AUTHORIZATION: {
                            return "AUTHORIZATION";
                        }

                        case CAPTURE: {
                            return "CAPTURE";
                        }

                        case CHANGE: {
                            return "CHANGE";
                        }

                        case EMV_AUTHORIZATION: {
                            return "EMV_AUTHORIZATION";
                        }

                        case SALE: {
                            return "SALE";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public enum TransactionStatus {
                ERROR,

                FAILURE,

                PENDING,

                SUCCESS,

                UNKNOWN_VALUE;

                public static TransactionStatus fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "ERROR": {
                            return ERROR;
                        }

                        case "FAILURE": {
                            return FAILURE;
                        }

                        case "PENDING": {
                            return PENDING;
                        }

                        case "SUCCESS": {
                            return SUCCESS;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case ERROR: {
                            return "ERROR";
                        }

                        case FAILURE: {
                            return "FAILURE";
                        }

                        case PENDING: {
                            return "PENDING";
                        }

                        case SUCCESS: {
                            return "SUCCESS";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }

            public interface UserErrorQueryDefinition {
                void define(UserErrorQuery _queryBuilder);
            }

            /**
            * Represents an error in the input of a mutation.
            */
            public static class UserErrorQuery extends Query<UserErrorQuery> {
                UserErrorQuery(StringBuilder _queryBuilder) {
                    super(_queryBuilder);
                }

                /**
                * Path to the input field which caused the error.
                */
                public UserErrorQuery field() {
                    startField("field");

                    return this;
                }

                /**
                * The error message.
                */
                public UserErrorQuery message() {
                    startField("message");

                    return this;
                }
            }

            /**
            * Represents an error in the input of a mutation.
            */
            public static class UserError extends AbstractResponse<UserError> implements DisplayableError {
                public UserError() {
                }

                public UserError(JsonObject fields) throws SchemaViolationError {
                    for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                        String key = field.getKey();
                        String fieldName = getFieldName(key);
                        switch (fieldName) {
                            case "field": {
                                List<String> optional1 = null;
                                if (!field.getValue().isJsonNull()) {
                                    List<String> list1 = new ArrayList<>();
                                    for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                        list1.add(jsonAsString(element1, key));
                                    }

                                    optional1 = list1;
                                }

                                responseData.put(key, optional1);

                                break;
                            }

                            case "message": {
                                responseData.put(key, jsonAsString(field.getValue(), key));

                                break;
                            }

                            case "__typename": {
                                responseData.put(key, jsonAsString(field.getValue(), key));
                                break;
                            }
                            default: {
                                throw new SchemaViolationError(this, key, field.getValue());
                            }
                        }
                    }
                }

                public String getGraphQlTypeName() {
                    return "UserError";
                }

                /**
                * Path to the input field which caused the error.
                */

                public List<String> getField() {
                    return (List<String>) get("field");
                }

                public UserError setField(List<String> arg) {
                    optimisticData.put(getKey("field"), arg);
                    return this;
                }

                /**
                * The error message.
                */

                public String getMessage() {
                    return (String) get("message");
                }

                public UserError setMessage(String arg) {
                    optimisticData.put(getKey("message"), arg);
                    return this;
                }

                public boolean unwrapsToObject(String key) {
                    switch (getFieldName(key)) {
                        case "field": return false;

                        case "message": return false;

                        default: return false;
                    }
                }
            }

            /**
            * Units of measurement for weight.
            */
            public enum WeightUnit {
                /**
                * Metric system unit of mass
                */
                GRAMS,

                /**
                * 1 kilogram equals 1000 grams
                */
                KILOGRAMS,

                /**
                * Imperial system unit of mass
                */
                OUNCES,

                /**
                * 1 pound equals 16 ounces
                */
                POUNDS,

                UNKNOWN_VALUE;

                public static WeightUnit fromGraphQl(String value) {
                    if (value == null) {
                        return null;
                    }

                    switch (value) {
                        case "GRAMS": {
                            return GRAMS;
                        }

                        case "KILOGRAMS": {
                            return KILOGRAMS;
                        }

                        case "OUNCES": {
                            return OUNCES;
                        }

                        case "POUNDS": {
                            return POUNDS;
                        }

                        default: {
                            return UNKNOWN_VALUE;
                        }
                    }
                }
                public String toString() {
                    switch (this) {
                        case GRAMS: {
                            return "GRAMS";
                        }

                        case KILOGRAMS: {
                            return "KILOGRAMS";
                        }

                        case OUNCES: {
                            return "OUNCES";
                        }

                        case POUNDS: {
                            return "POUNDS";
                        }

                        default: {
                            return "";
                        }
                    }
                }
            }
        }
