let { generateService } = require('@umijs/openapi');

generateService({
    requestLibPath: "import request  from '@/utils/request'",
    schemaPath: 'http://127.0.0.1:8101/api/v3/api-docs/default',
    serversPath: './src/servers'
});
