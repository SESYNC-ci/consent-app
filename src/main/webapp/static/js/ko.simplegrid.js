// based on the grid from: http://knockoutjs.com/examples/resources/knockout.simpleGrid.3.0.js
// configuration: columns: {headerText, rowText (fn or property name,) headerClass, cellTemplate}
(function () {
    // Private function
    function getColumnsForScaffolding(data) {
        if ((typeof data.length !== 'number') || data.length === 0) {
            return [];
        }
        var columns = [];
        for (var propertyName in data[0]) {
            columns.push({headerText: propertyName, rowText: propertyName});
        }
        return columns;
    }
    var templateEngine = new ko.nativeTemplateEngine();

    ko.simpleGrid = {
        // Defines a view model class you can use to populate a grid
        viewModel: function (configuration) {
            var self = this;
            self.data = configuration.data;
            self.currentPageIndex = ko.observable(0);
            self.pageSize = ko.computed(function () {
                if (configuration.pageSize) {
                    if (ko.isObservable(configuration.pageSize)) {
                        return configuration.pageSize();
                    } else {
                        return configuration.pageSize;
                    }
                } else {
                    return 5;
                }
            });

            // If you don't specify columns configuration, we'll use scaffolding
            self.columns = configuration.columns || getColumnsForScaffolding(ko.unwrap(self.data));
            self.columns.forEach(function (v) {
                if (typeof v.cellTemplate === 'undefined') {
                    v.templateName = 'ko_simpleGrid_celldefault';
                } else {
                    v.templateName = v.cellTemplate;
                }
            });

            this.itemsOnCurrentPage = ko.computed(function () {
                if (self.pageSize() === -1) {
                    return ko.unwrap(self.data);
                } else {
                    var startIndex = self.pageSize() * this.currentPageIndex();
                    var ret = ko.unwrap(self.data).slice(startIndex, startIndex + self.pageSize());
                    return ret;
                }
            }, this);

            this.maxPageIndex = ko.computed(function () {
                return Math.ceil(ko.unwrap(self.data).length / this.pageSize()) - 1;
            }, this);
        }
    };

    // Templates used to render the grid


    templateEngine.addTemplate = function (templateName, templateMarkup) {
        document.write("<script type='text/html' id='" + templateName + "'>" + templateMarkup + "<" + "/script>");
    };

    templateEngine.addTemplate("ko_simpleGrid_grid", "\
                    <table class=\"table table-bordered table-striped\" cellspacing=\"0\">\
                        <thead>\
                            <tr data-bind=\"foreach: columns\">\
                               <th  data-bind=\"text: headerText, css: headerClass\"></th>\
                            </tr>\
                        </thead>\
                        <tbody data-bind=\"foreach: itemsOnCurrentPage\">\
                           <tr data-bind=\"foreach: $parent.columns\">\
                               <td data-bind=\"template: {name: templateName, data: {column: $data, data: $parent}}\"></td>\
                            </tr>\
                        </tbody>\
                    </table>");
    templateEngine.addTemplate("ko_simpleGrid_pageLinks", "\
                    <div class=\"btn-group\">\
                        <!-- ko foreach: ko.utils.range(0, maxPageIndex) -->\
                        <button class=\"btn btn-default\" data-bind=\"text: $data + 1, click: function() { $root.currentPageIndex($data) }, enable: ($data != $root.currentPageIndex())\"></button>\
                        <!-- /ko -->\
                    </div>");
    templateEngine.addTemplate("ko_simpleGrid_celldefault", "\
                    <div data-bind=\"html: typeof column.rowText == 'function' ? column.rowText(data) : data[column.rowText] \"></div>\
            ");

    // The "simpleGrid" binding
    ko.bindingHandlers.simpleGrid = {
        init: function () {
            return {'controlsDescendantBindings': true};
        },
        // This method is called to initialize the node, and will also be called again if you change what the grid is bound to
        update: function (element, viewModelAccessor, allBindings) {
            var viewModel = viewModelAccessor();

            // Empty the element
            while (element.firstChild)
                ko.removeNode(element.firstChild);

            // Allow the default templates to be overridden
            var gridTemplateName = allBindings.get('simpleGridTemplate') || "ko_simpleGrid_grid";

            // Render the main grid
            var gridContainer = element.appendChild(document.createElement("DIV"));
            ko.renderTemplate(gridTemplateName, viewModel, {templateEngine: templateEngine}, gridContainer, "replaceNode");

        }
    };
    // The "simpleGridPager" binding
    ko.bindingHandlers.simpleGridPager = {
        init: function (element, valueAccessor, allBindings, viewModel, bindingContext) {
            return {'controlsDescendantBindings': true};
        },
        // This method is called to initialize the node, and will also be called again if you change what the grid is bound to
        update: function (element, viewModelAccessor, allBindings) {
            var viewModel = viewModelAccessor();

            // Empty the element
            while (element.firstChild)
                ko.removeNode(element.firstChild);

            // Allow the default templates to be overridden
            var pageLinksTemplateName = allBindings.get('simpleGridPagerTemplate') || "ko_simpleGrid_pageLinks";

            // Render the page links
            var pageLinksContainer = element.appendChild(document.createElement("DIV"));
            ko.renderTemplate(pageLinksTemplateName, viewModel, {templateEngine: templateEngine}, pageLinksContainer, "replaceNode");
        }
    };
})();