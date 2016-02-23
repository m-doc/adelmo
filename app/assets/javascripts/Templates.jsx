var TemplateBox = React.createClass({
  getInitialState: function() {
    return {data: []};
  },
  componentDidMount: function() {
    $.ajax({
      url: this.props.url,
      dataType: 'json',
      cache: false,
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  render: function() {
    return (
      <div className="templateBox">
        <strong>Templates</strong>
        <TemplateList data={this.state.data} />
      </div>
    );
  }
});

var TemplateList = React.createClass({
  render: function() {
    var templateNodes = this.props.data.map(function(template) {
      return (
        <Template name={template.name} />
      );
    });
    return (
      <div className="templateList">
        {templateNodes}
      </div>
    );
  }
});

var Template = React.createClass({
  render: function() {
    return (
      <div className="template">
        {this.props.name}
      </div>
    );
  }
});

ReactDOM.render(
  <TemplateBox url="/api/templates" />,
  document.getElementById('templates')
);
